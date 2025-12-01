package pkg.maid_to_order.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import pkg.maid_to_order.R
import pkg.maid_to_order.data.Screen
import pkg.maid_to_order.utils.VibrationUtils
import pkg.maid_to_order.viewmodel.CartViewModel
import pkg.maid_to_order.viewmodel.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    formViewModel: FormViewModel = viewModel()
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // Estados de los campos
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var tableNumber by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Estados de validación
    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var tableError by remember { mutableStateOf<String?>(null) }

    // Estados de validación exitosa (para iconos verdes)
    var nameValid by remember { mutableStateOf(false) }
    var phoneValid by remember { mutableStateOf(false) }
    var tableValid by remember { mutableStateOf(false) }

    // Funciones de validación
    fun validateName(value: String): Pair<Boolean, String?> {
        return when {
            value.isBlank() -> false to "El nombre es obligatorio"
            value.length < 3 -> false to "El nombre debe tener al menos 3 caracteres"
            !value.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) ->
                false to "El nombre solo debe contener letras"
            else -> true to null
        }
    }

    fun validatePhone(value: String): Pair<Boolean, String?> {
        return when {
            value.isBlank() -> false to "El teléfono es obligatorio"
            !value.matches(Regex("^[0-9+\\-() ]+$")) ->
                false to "El teléfono solo debe contener números"
            value.replace(Regex("[^0-9]"), "").length < 8 ->
                false to "El teléfono debe tener al menos 8 dígitos"
            else -> true to null
        }
    }

    fun validateTable(value: String): Pair<Boolean, String?> {
        return when {
            value.isBlank() -> false to "El número de mesa es obligatorio"
            !value.matches(Regex("^[0-9]+$")) ->
                false to "Solo se permiten números"
            value.toIntOrNull() == null || value.toInt() < 1 ->
                false to "Número de mesa inválido"
            else -> true to null
        }
    }

    fun validateAll(): Boolean {
        val (nValid, nError) = validateName(name)
        val (pValid, pError) = validatePhone(phone)
        val (tValid, tError) = validateTable(tableNumber)

        nameError = nError
        phoneError = pError
        tableError = tError

        nameValid = nValid
        phoneValid = pValid
        tableValid = tValid

        return nValid && pValid && tValid
    }

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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
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
                Spacer(modifier = Modifier.height(16.dp))

                // Total del pedido
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Total a Pagar:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "$${"%.2f".format(cartViewModel.total.value)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Campo Número de Mesa
                OutlinedTextField(
                    value = tableNumber,
                    onValueChange = {
                        tableNumber = it
                        val (valid, error) = validateTable(it)
                        tableValid = valid
                        tableError = if (it.isNotBlank()) error else null
                    },
                    label = { Text("Número de Mesa *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = tableError != null,
                    trailingIcon = {
                        when {
                            tableError != null -> Icon(
                                Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                            tableValid -> Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Válido",
                                tint = Color(0xFF4CAF50)
                            )
                        }
                    },
                    supportingText = {
                        tableError?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                // Campo Nombre
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        val (valid, error) = validateName(it)
                        nameValid = valid
                        nameError = if (it.isNotBlank()) error else null
                    },
                    label = { Text("Nombre del Cliente *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError != null,
                    trailingIcon = {
                        when {
                            nameError != null -> Icon(
                                Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                            nameValid -> Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Válido",
                                tint = Color(0xFF4CAF50)
                            )
                        }
                    },
                    supportingText = {
                        nameError?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                // Campo Teléfono
                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        val (valid, error) = validatePhone(it)
                        phoneValid = valid
                        phoneError = if (it.isNotBlank()) error else null
                    },
                    label = { Text("Teléfono de Contacto *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = phoneError != null,
                    trailingIcon = {
                        when {
                            phoneError != null -> Icon(
                                Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                            phoneValid -> Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Válido",
                                tint = Color(0xFF4CAF50)
                            )
                        }
                    },
                    supportingText = {
                        phoneError?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                // Campo Notas
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas / Comentarios (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón confirmar con animación
                var confirmPressed by remember { mutableStateOf(false) }
                val confirmScale by animateFloatAsState(
                    targetValue = if (confirmPressed) 0.95f else 1f,
                    animationSpec = tween(150),
                    label = "confirmScale"
                )

                Button(
                    onClick = {
                        if (validateAll()) {
                            confirmPressed = true

                            // ⭐ VIBRACIÓN DE ÉXITO ⭐
                            VibrationUtils.vibrateSuccess(context)

                            // Actualizar FormViewModel
                            formViewModel.updateTableNumber(tableNumber)
                            formViewModel.updateNotes(notes)

                            // Crear orden
                            val order = formViewModel.createOrder(
                                cartViewModel.cartItems.toList(),
                                cartViewModel.total.value
                            )

                            showDialog = true
                        } else {
                            // ⭐ VIBRACIÓN DE ERROR ⭐
                            VibrationUtils.vibrateError(context)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .graphicsLayer(scaleX = confirmScale, scaleY = confirmScale),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Confirmar Pedido",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    "* Campos obligatorios",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // Diálogo de confirmación
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                icon = {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(48.dp)
                    )
                },
                title = {
                    Text(
                        "¡Pedido Confirmado!",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        Text("Tu pedido ha sido registrado con éxito.")
                        Spacer(Modifier.height(8.dp))
                        Text("Mesa: $tableNumber")
                        Text("Cliente: $name")
                        Text("Teléfono: $phone")
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Te contactaremos pronto para coordinar la entrega.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            cartViewModel.clearCart()
                            showDialog = false
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}