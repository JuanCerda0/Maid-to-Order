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
    var phone by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var address by mutableStateOf("")
        private set

    var nameError by mutableStateOf<String?>(null)
        private set
    var phoneError by mutableStateOf<String?>(null)
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var addressError by mutableStateOf<String?>(null)
        private set

    fun updateName(value: String) {
        name = value
        validateName()
    }

    fun updatePhone(value: String) {
        phone = value
        validatePhone()
    }

    fun updateEmail(value: String) {
        email = value
        validateEmail()
    }

    fun updateAddress(value: String) {
        address = value
        validateAddress()
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

    private fun validatePhone(): Boolean {
        return if (phone.isBlank()) {
            phoneError = "El teléfono es requerido"
            false
        } else if (!phone.matches(Regex("^[0-9]{9}$"))) {
            phoneError = "El teléfono debe tener 9 dígitos"
            false
        } else {
            phoneError = null
            true
        }
    }

    private fun validateEmail(): Boolean {
        return if (email.isBlank()) {
            emailError = "El email es requerido"
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Email inválido"
            false
        } else {
            emailError = null
            true
        }
    }

    private fun validateAddress(): Boolean {
        return if (address.isBlank()) {
            addressError = "La dirección es requerida"
            false
        } else {
            addressError = null
            true
        }
    }

    fun validateForm(): Boolean {
        val nameValid = validateName()
        val phoneValid = validatePhone()
        val emailValid = validateEmail()
        val addressValid = validateAddress()
        return nameValid && phoneValid && emailValid && addressValid
    }

    fun createOrder(cartItems: List<CartItem>, total: Double): Order {
        return Order(
            items = cartItems.map { OrderItem(it.dish, it.quantity) },
            customerName = name,
            customerPhone = phone,
            customerEmail = email,
            deliveryAddress = address,
            total = total
        )
    }
}