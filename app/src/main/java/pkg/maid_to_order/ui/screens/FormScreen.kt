package pkg.maid_to_order.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pkg.maid_to_order.data.Screen
import pkg.maid_to_order.viewmodel.CartViewModel
import pkg.maid_to_order.viewmodel.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    formViewModel: FormViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de Entrega") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = formViewModel.name,
                onValueChange = { formViewModel.updateName(it) },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                isError = formViewModel.nameError != null,
                supportingText = { formViewModel.nameError?.let { Text(it) } }
            )

            OutlinedTextField(
                value = formViewModel.phone,
                onValueChange = { formViewModel.updatePhone(it) },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = formViewModel.phoneError != null,
                supportingText = { formViewModel.phoneError?.let { Text(it) } }
            )

            OutlinedTextField(
                value = formViewModel.email,
                onValueChange = { formViewModel.updateEmail(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = formViewModel.emailError != null,
                supportingText = { formViewModel.emailError?.let { Text(it) } }
            )

            OutlinedTextField(
                value = formViewModel.address,
                onValueChange = { formViewModel.updateAddress(it) },
                label = { Text("Dirección de Entrega") },
                modifier = Modifier.fillMaxWidth(),
                isError = formViewModel.addressError != null,
                supportingText = { formViewModel.addressError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (formViewModel.validateForm()) {
                        formViewModel.createOrder(cartViewModel.cartItems.toList(), cartViewModel.total.value)
                        showDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Pedido")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("¡Pedido Confirmado!") },
                text = { Text("Tu pedido ha sido registrado con éxito. Te contactaremos pronto para coordinar la entrega.") },
                confirmButton = {
                    Button(
                        onClick = {
                            cartViewModel.clearCart()
                            showDialog = false
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}
