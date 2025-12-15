package pkg.maid_to_order.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pkg.maid_to_order.R
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
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_maid),
                            contentDescription = "Logo",
                            modifier = Modifier.size(60.dp)
                        )
                        Text("Datos de Entrega", fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Fondo con opacidad del 50%
            Image(
                painter = painterResource(id = R.drawable.fondo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = 0.5f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = formViewModel.tableNumber,
                onValueChange = { formViewModel.updateTableNumber(it) },
                label = { Text("Número de Mesa") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = formViewModel.tableError != null,
                supportingText = { formViewModel.tableError?.let { Text(it) } },
                singleLine = true
            )

            OutlinedTextField(
                value = formViewModel.notes,
                onValueChange = { formViewModel.updateNotes(it) },
                label = { Text("Notas / Comentarios (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            var confirmPressed by remember { mutableStateOf(false) }
            val confirmScale by animateFloatAsState(
                targetValue = if (confirmPressed) 0.9f else 1f,
                animationSpec = tween(150), label = "confirmScale"
            )

            Button(
                onClick = {
                    if (formViewModel.validateForm()) {
                        confirmPressed = true
                        formViewModel.submitOrderToApi(cartViewModel.cartItems.toList())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(scaleX = confirmScale, scaleY = confirmScale),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Confirmar Pedido", fontSize = 18.sp)
            }
        }

        // Observar el estado del envío
        LaunchedEffect(formViewModel.submitSuccess.value) {
            if (formViewModel.submitSuccess.value) {
                showDialog = true
            }
        }
        
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("¡Pedido Confirmado!") },
                text = { 
                    Text(
                        if (formViewModel.submitError.value != null) {
                            "Error: ${formViewModel.submitError.value}"
                        } else {
                            "Tu pedido ha sido registrado con éxito. Te contactaremos pronto para coordinar la entrega."
                        }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            cartViewModel.clearCart()
                            showDialog = false
                            formViewModel.submitSuccess.value = false
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
}
