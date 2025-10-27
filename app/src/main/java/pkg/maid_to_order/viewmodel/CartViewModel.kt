package pkg.maid_to_order.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import pkg.maid_to_order.data.model.Dish

class CartViewModel : ViewModel() {
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: SnapshotStateList<CartItem> = _cartItems

    private val _total = mutableStateOf(0.0)
    val total = _total

    fun addToCart(dish: Dish) {
        val existingItem = _cartItems.find { it.dish.id == dish.id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            _cartItems.add(CartItem(dish, 1))
        }
        updateTotal()
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
        }
    }

    fun clearCart() {
        _cartItems.clear()
        updateTotal()
    }

    private fun updateTotal() {
        _total.value = _cartItems.sumOf { it.dish.price * it.quantity }
    }

    data class CartItem(
        val dish: Dish,
        var quantity: Int
    )
}