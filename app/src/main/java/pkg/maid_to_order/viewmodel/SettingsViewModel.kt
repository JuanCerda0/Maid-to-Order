package pkg.maid_to_order.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pkg.maid_to_order.repository.dataStore

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    init {
        loadDarkModePreference()
    }

    private fun loadDarkModePreference() {
        viewModelScope.launch {
            try {
                val prefs = getApplication<Application>().applicationContext.dataStore.data.first()
                _isDarkMode.value = prefs[DARK_MODE_KEY] ?: false
            } catch (_: Exception) {
                // Si falla, usar valor por defecto
                _isDarkMode.value = false
            }
        }
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
        saveDarkModePreference()
    }

    private fun saveDarkModePreference() {
        viewModelScope.launch {
            try {
                getApplication<Application>().applicationContext.dataStore.edit { prefs ->
                    prefs[DARK_MODE_KEY] = _isDarkMode.value
                }
            } catch (_: Exception) {
                // Ignorar errores de persistencia
            }
        }
    }
}

