package pkg.maid_to_order.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pkg.maid_to_order.data.model.Dish
import pkg.maid_to_order.repository.dataStore
import pkg.maid_to_order.utils.VibrationUtils

/**
 * CartViewModel ahora persiste el carrito en DataStore (Preferences) como JSON.
 * Los items se serializan en una estructura ligera (id, name, description, price, imageRes, quantity)
 */
class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: SnapshotStateList<CartItem> = _cartItems

    private val _total = mutableStateOf(0.0)
    val total = _total

    private val gson = Gson()
    private val CART_KEY = stringPreferencesKey("cart_json")

    init {
        // Cargar carrito desde DataStore al iniciar
        viewModelScope.launch {
            try {
                val prefs = getApplication<Application>().applicationContext.dataStore.data.first()
                val json = prefs[CART_KEY]
                if (!json.isNullOrBlank()) {
                    val listType = object : TypeToken<List<CartItemDto>>() {}.type
                    val dtoList: List<CartItemDto> = gson.fromJson(json, listType)
                    dtoList.forEach { dto ->
                        _cartItems.add(
                            CartItem(
                                Dish(
                                    id = dto.id,
                                    name = dto.name,
                                    description = dto.description,
                                    price = dto.price,
                                    category = dto.category ?: "General",
                                    imageRes = dto.imageRes,
                                    imageUri = dto.imageUri
                                ),
                                dto.quantity
                            )
                        )
                    }
                    updateTotal()
                }
            } catch (_: Exception) {
                // If reading fails, start with empty cart
            }
        }
    }

    fun addToCart(dish: Dish) {
        val existingItem = _cartItems.find { it.dish.id == dish.id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            _cartItems.add(CartItem(dish, 1))
        }
        updateTotal()
        persistCart()
        VibrationUtils.vibrateShort(getApplication())
    }

    fun removeFromCart(dish: Dish) {
        val item = _cartItems.find { it.dish.id == dish.id }
        item?.let {
            if (it.quantity > 1) {
                it.quantity--
            } else {
                _cartItems.remove(it)
            }
            updateTotal()
            persistCart()
            VibrationUtils.vibrateShort(getApplication())
        }
    }

    fun clearCart() {
        _cartItems.clear()
        updateTotal()
        persistCart()
        VibrationUtils.vibrateMedium(getApplication())
    }

    private fun updateTotal() {
        _total.value = _cartItems.sumOf { it.dish.price * it.quantity }
    }

    private fun persistCart() {
        viewModelScope.launch {
            try {
                val dtoList = _cartItems.map { CartItemDto.fromCartItem(it) }
                val json = gson.toJson(dtoList)
                getApplication<Application>().applicationContext.dataStore.edit { prefs ->
                    prefs[CART_KEY] = json
                }
            } catch (_: Exception) {
                // ignore persistence errors for now
            }
        }
    }

    data class CartItem(
        val dish: Dish,
        var quantity: Int
    )

    private data class CartItemDto(
        val id: Int,
        val name: String,
        val description: String,
        val price: Double,
        val category: String? = null,
        val imageRes: Int? = null,
        val imageUri: String? = null,
        val quantity: Int
    ) {
        companion object {
            fun fromCartItem(item: CartItem) = CartItemDto(
                id = item.dish.id,
                name = item.dish.name,
                description = item.dish.description,
                price = item.dish.price,
                category = item.dish.category,
                imageRes = item.dish.imageRes,
                imageUri = item.dish.imageUri,
                quantity = item.quantity
            )
        }
    }
}