package pkg.maid_to_order.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pkg.maid_to_order.data.model.Order
import pkg.maid_to_order.data.model.OrderItem
import pkg.maid_to_order.viewmodel.CartViewModel.CartItem

class FormViewModel : ViewModel() {
    // Solo número de mesa para pedidos en mesa
    var tableNumber by mutableStateOf("")
        private set
    var notes by mutableStateOf("")
        private set

    var tableError by mutableStateOf<String?>(null)
        private set

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
}