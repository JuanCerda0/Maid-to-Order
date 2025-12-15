package pkg.maid_to_order.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pkg.maid_to_order.network.api.WeatherClient
import pkg.maid_to_order.network.dto.WeatherResponse

sealed interface WeatherUiState {
    object Loading : WeatherUiState
    data class Success(
        val temperature: Double,
        val windSpeed: Double,
        val time: String
    ) : WeatherUiState

    data class Error(val message: String) : WeatherUiState
}

class WeatherViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    init {
        refreshWeather()
    }

    fun refreshWeather(latitude: Double = -33.45, longitude: Double = -70.66) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val response = WeatherClient.api.getCurrentWeather(latitude, longitude)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = response.body().toSuccessState()
                } else {
                    _uiState.value = WeatherUiState.Error("No se pudo obtener el clima (${response.code()})")
                }
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    private fun WeatherResponse?.toSuccessState(): WeatherUiState {
        val current = this?.currentWeather
        return if (current != null) {
            WeatherUiState.Success(
                temperature = current.temperature,
                windSpeed = current.windSpeed,
                time = current.time
            )
        } else {
            WeatherUiState.Error("Datos de clima no disponibles")
        }
    }
}
