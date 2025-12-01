package pkg.maid_to_order.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pkg.maid_to_order.data.model.FoodItem
import pkg.maid_to_order.repository.NutritionRepository

class NutritionViewModel : ViewModel() {
    private val repository = NutritionRepository()

    private val _searchResults = mutableStateOf<List<FoodItem>>(emptyList())
    val searchResults: State<List<FoodItem>> = _searchResults

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun searchFoods(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.searchFoods(query)
                .onSuccess { foods ->
                    _searchResults.value = foods
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Error desconocido"
                }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
