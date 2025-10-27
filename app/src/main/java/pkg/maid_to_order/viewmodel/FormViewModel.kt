package pkg.maid_to_order.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pkg.maid_to_order.data.model.Order
import pkg.maid_to_order.data.model.OrderItem
import pkg.maid_to_order.viewmodel.CartViewModel.CartItem

class FormViewModel : ViewModel() {
    var name by mutableStateOf("")
        private set
    var tableNumber by mutableStateOf("")
        private set
    var notes by mutableStateOf("")
        private set

    var nameError by mutableStateOf<String?>(null)
        private set
    var tableError by mutableStateOf<String?>(null)
        private set

    fun updateName(value: String) {
        name = value
        validateName()
    }

    fun updateTableNumber(value: String) {
        tableNumber = value
        validateTableNumber()
    }

    fun updateNotes(value: String) {
        notes = value
    }

    private fun validateName(): Boolean {
        return if (name.isBlank()) {
            nameError = "El nombre es requerido"
            false
        } else {
            nameError = null
            true
        }
    }

    private fun validateTableNumber(): Boolean {
        return if (tableNumber.isBlank()) {
            tableError = "El número de mesa es requerido"
            false
        } else {
            tableError = null
            true
        }
    }

    fun validateForm(): Boolean {
        val nameValid = validateName()
        val tableValid = validateTableNumber()
        return nameValid && tableValid
    }

    fun createOrder(cartItems: List<CartItem>, total: Double): Order {
        return Order(
            items = cartItems.map { OrderItem(it.dish, it.quantity) },
            customerName = name,
            tableNumber = tableNumber,
            notes = if (notes.isBlank()) null else notes,
            total = total
        )
    }
}