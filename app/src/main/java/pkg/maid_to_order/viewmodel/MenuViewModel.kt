package pkg.maid_to_order.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pkg.maid_to_order.data.model.Dish
import pkg.maid_to_order.network.api.RetrofitClient
import pkg.maid_to_order.network.mapper.DishMapper
import pkg.maid_to_order.network.mapper.SpecialDishMapper

class MenuViewModel : ViewModel() {

    private val _menuList = mutableStateListOf<Dish>()
    val menuList: SnapshotStateList<Dish> get() = _menuList
    
    private val _specialDishes = mutableStateListOf<Dish>()
    val specialDishes: SnapshotStateList<Dish> get() = _specialDishes
    
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    init {
        loadMenuFromApi()
        loadSpecialDishes()
    }
    
    private fun loadMenuFromApi() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val response = RetrofitClient.api.getDishes()
                if (response.isSuccessful && response.body() != null) {
                    val dishes = DishMapper.toDomainList(response.body()!!)
                    _menuList.clear()
                    _menuList.addAll(dishes)
                } else {
                    // Fallback a datos locales si la API falla
                    loadLocalMenu()
                }
            } catch (e: Exception) {
                errorMessage.value = "Error al cargar el menú: ${e.message}"
                // Fallback a datos locales
                loadLocalMenu()
            } finally {
                isLoading.value = false
            }
        }
    }
    
    private fun loadLocalMenu() {
        _menuList.addAll(
            listOf(
                Dish(1, "Katsudon", "Cerdo frito con arroz y huevo", 6700.0, "Platos Principales", null, null),
                Dish(2, "Ramen", "Sopa japonesa con fideos y cerdo", 7400.0, "Sopas", null, null),
                Dish(3, "Onigiri", "Bola de arroz rellena con salmón", 4000.0, "Aperitivos", null, null),
                Dish(4, "Curry Japonés", "Arroz con curry suave y carne", 8500.0, "Platos Principales", null, null)
            )
        )
    }
    
    private fun loadSpecialDishes() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getSpecialDishes(today = true)
                if (response.isSuccessful && response.body() != null) {
                    val dishes = SpecialDishMapper.toDomainList(response.body()!!)
                    _specialDishes.clear()
                    _specialDishes.addAll(dishes)
                }
            } catch (e: Exception) {
                // Silenciar errores de platos especiales
            }
        }
    }
    
    fun refreshMenu() {
        loadMenuFromApi()
        loadSpecialDishes()
    }

    fun loadMenu(): List<Dish> = _menuList
    
    fun getMenuByCategory(category: String?): List<Dish> {
        return if (category == null || category == "Todos") {
            _menuList
        } else {
            _menuList.filter { it.category == category }
        }
    }
    
    fun getCategories(): List<String> {
        return listOf("Todos") + _menuList.map { it.category }.distinct().sorted()
    }

    fun getDishById(id: Int): Dish? = _menuList.find { it.id == id }
    
    fun searchDishes(query: String): List<Dish> {
        if (query.isBlank()) return _menuList
        val lowerQuery = query.lowercase()
        return _menuList.filter {
            it.name.lowercase().contains(lowerQuery) ||
            it.description.lowercase().contains(lowerQuery) ||
            it.category.lowercase().contains(lowerQuery)
        }
    }
    
    fun addDish(dish: Dish) {
        val newId = (_menuList.maxOfOrNull { it.id } ?: 0) + 1
        _menuList.add(dish.copy(id = newId))
    }
    
    fun updateDish(dish: Dish) {
        val index = _menuList.indexOfFirst { it.id == dish.id }
        if (index != -1) {
            _menuList[index] = dish
        }
    }
    
    fun deleteDish(dishId: Int) {
        _menuList.removeAll { it.id == dishId }
    }
}
