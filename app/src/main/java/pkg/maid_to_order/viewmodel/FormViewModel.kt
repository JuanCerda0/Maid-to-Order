package pkg.maid_to_order.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pkg.maid_to_order.data.model.Order
import pkg.maid_to_order.data.model.OrderItem
import pkg.maid_to_order.network.api.RetrofitClient
import pkg.maid_to_order.network.dto.OrderCreateDto
import pkg.maid_to_order.network.dto.OrderItemCreateDto
import pkg.maid_to_order.viewmodel.CartViewModel.CartItem

class FormViewModel : ViewModel() {
    // Solo número de mesa para pedidos en mesa
    var tableNumber by mutableStateOf("")
        private set
    var notes by mutableStateOf("")
        private set

    var tableError by mutableStateOf<String?>(null)
        private set
    
    val isSubmitting = mutableStateOf(false)
    val submitError = mutableStateOf<String?>(null)
    val submitSuccess = mutableStateOf(false)

    fun updateTableNumber(value: String) {
        tableNumber = value
        validateTable()
    }

    fun updateNotes(value: String) {
        notes = value
    }

    private fun validateTable(): Boolean {
        return if (tableNumber.isBlank()) {
            tableError = "El número de mesa es requerido"
            false
        } else if (!tableNumber.matches(Regex("^[0-9]{1,4}$"))) {
            tableError = "Número de mesa inválido"
            false
        } else {
            tableError = null
            true
        }
    }

    fun validateForm(): Boolean {
        return validateTable()
    }

    fun createOrder(cartItems: List<CartItem>, total: Double): Order {
        return Order(
            items = cartItems.map { OrderItem(it.dish, it.quantity) },
            customerName = null, // No se requiere nombre, solo mesa
            total = total,
            tableNumber = tableNumber,
            notes = notes.ifBlank { null }
        )
    }
    
    fun submitOrderToApi(cartItems: List<CartItem>) {
        viewModelScope.launch {
            isSubmitting.value = true
            submitError.value = null
            submitSuccess.value = false
            
            try {
                val orderDto = OrderCreateDto(
                    items = cartItems.map { 
                        OrderItemCreateDto(
                            dishId = it.dish.id.toLong(),
                            quantity = it.quantity
                        )
                    },
                    customerPhone = "0000000000", // Placeholder
                    tableNumber = tableNumber,
                    notes = notes.ifBlank { null }
                )
                
                val response = RetrofitClient.api.createOrder(orderDto)
                if (response.isSuccessful) {
                    submitSuccess.value = true
                } else {
                    submitError.value = "Error al crear el pedido"
                }
            } catch (e: Exception) {
                submitError.value = "Error de conexión: ${e.message}"
            } finally {
                isSubmitting.value = false
            }
        }
    }
}