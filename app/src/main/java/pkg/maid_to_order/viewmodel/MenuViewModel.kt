package pkg.maid_to_order.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pkg.maid_to_order.data.model.Dish
import pkg.maid_to_order.network.api.RetrofitClient
import pkg.maid_to_order.network.dto.DishCreateDto
import pkg.maid_to_order.network.mapper.DishMapper
import pkg.maid_to_order.network.mapper.SpecialDishMapper

class MenuViewModel : ViewModel() {

    private val _menuList = mutableStateListOf<Dish>()
    val menuList: SnapshotStateList<Dish> get() = _menuList
    
    private val _specialDishes = mutableStateListOf<Dish>()
    val specialDishes: SnapshotStateList<Dish> get() = _specialDishes
    
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    data class AdminUiState(
        val isProcessing: Boolean = false,
        val message: String? = null,
        val isSuccess: Boolean = false,
        val eventId: Long = 0L
    )

    private val _adminUiState = MutableStateFlow(AdminUiState())
    val adminUiState: StateFlow<AdminUiState> = _adminUiState

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
        if (_menuList.isEmpty()) {
            _menuList.addAll(
            listOf(
                Dish(1, "Katsudon", "Cerdo frito con arroz y huevo", 6700.0, "Platos Principales", null, null),
                Dish(2, "Ramen", "Sopa japonesa con fideos y cerdo", 7400.0, "Sopas", null, null),
                Dish(3, "Onigiri", "Bola de arroz rellena con salmón", 4000.0, "Aperitivos", null, null),
                Dish(4, "Curry Japonés", "Arroz con curry suave y carne", 8500.0, "Platos Principales", null, null)
            )
        )
        }
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
    
    fun submitDish(dish: Dish, isEdit: Boolean) {
        viewModelScope.launch {
            _adminUiState.value = AdminUiState(isProcessing = true)
            try {
                val dto = dish.toCreateDto()
                val response = if (isEdit) {
                    RetrofitClient.api.updateDish(dish.id.toLong(), dto)
                } else {
                    RetrofitClient.api.createDish(dto)
                }
                if (response.isSuccessful) {
                    refreshMenu()
                    _adminUiState.value = AdminUiState(
                        isProcessing = false,
                        message = if (isEdit) "Platillo actualizado correctamente" else "Platillo creado correctamente",
                        isSuccess = true,
                        eventId = System.currentTimeMillis()
                    )
                } else {
                    _adminUiState.value = AdminUiState(
                        isProcessing = false,
                        message = "Error del servidor (${response.code()})",
                        isSuccess = false,
                        eventId = System.currentTimeMillis()
                    )
                }
            } catch (e: Exception) {
                _adminUiState.value = AdminUiState(
                    isProcessing = false,
                    message = "Error al guardar: ${e.message}",
                    isSuccess = false,
                    eventId = System.currentTimeMillis()
                )
            }
        }
    }

    fun deleteDishRemote(dishId: Int) {
        viewModelScope.launch {
            _adminUiState.value = AdminUiState(isProcessing = true)
            try {
                val response = RetrofitClient.api.deleteDish(dishId.toLong())
                if (response.isSuccessful) {
                    refreshMenu()
                    _adminUiState.value = AdminUiState(
                        isProcessing = false,
                        message = "Platillo eliminado",
                        isSuccess = true,
                        eventId = System.currentTimeMillis()
                    )
                } else {
                    _adminUiState.value = AdminUiState(
                        isProcessing = false,
                        message = "No se pudo eliminar (${response.code()})",
                        isSuccess = false,
                        eventId = System.currentTimeMillis()
                    )
                }
            } catch (e: Exception) {
                _adminUiState.value = AdminUiState(
                    isProcessing = false,
                    message = "Error al eliminar: ${e.message}",
                    isSuccess = false,
                    eventId = System.currentTimeMillis()
                )
            }
        }
    }

    fun consumeAdminMessage() {
        _adminUiState.value = _adminUiState.value.copy(message = null, eventId = 0L, isSuccess = false)
    }

    private fun Dish.toCreateDto(): DishCreateDto {
        return DishCreateDto(
            name = name,
            description = description,
            price = price,
            category = category,
            imageUrl = imageUri,
            available = true
        )
    }
}
