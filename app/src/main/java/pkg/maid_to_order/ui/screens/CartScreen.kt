package pkg.maid_to_order.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pkg.maid_to_order.R
import pkg.maid_to_order.data.Screen
import pkg.maid_to_order.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
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
                        Text("Carrito de Compras", fontWeight = FontWeight.Bold)
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
            ) {
            if (cartViewModel.cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "El carrito está vacío",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartViewModel.cartItems) { item ->
                        CartItemCard(
                            name = item.dish.name,
                            price = item.dish.price,
                            quantity = item.quantity,
                            onIncrement = { cartViewModel.addToCart(item.dish) },
                            onDecrement = { cartViewModel.removeFromCart(item.dish) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$${String.format("%.0f", cartViewModel.total.value)}",
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

            var proceedPressed by remember { mutableStateOf(false) }
            val proceedScale by animateFloatAsState(
                targetValue = if (proceedPressed) 0.9f else 1f,
                animationSpec = tween(150), label = "proceedScale"
            )

                Button(
                    onClick = {
                        proceedPressed = true
                        navController.navigate(Screen.Form.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(scaleX = proceedScale, scaleY = proceedScale),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Proceder al Pago", fontSize = 18.sp)
                }
            }
            }
        }
    }
}

@Composable
fun CartItemCard(
    name: String,
    price: Double,
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    var incrementPressed by remember { mutableStateOf(false) }
    var decrementPressed by remember { mutableStateOf(false) }

    val incrementScale by animateFloatAsState(
        targetValue = if (incrementPressed) 0.85f else 1f,
        animationSpec = tween(150), label = "incrementScale"
    )

    val decrementScale by animateFloatAsState(
        targetValue = if (decrementPressed) 0.85f else 1f,
        animationSpec = tween(150), label = "decrementScale"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = name, fontWeight = FontWeight.Medium)
                Text(text = "$${String.format("%.0f", price * quantity)}")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        decrementPressed = true
                        onDecrement()
                    },
                    modifier = Modifier.graphicsLayer(scaleX = decrementScale, scaleY = decrementScale)
                ) {
                    Text("-", fontSize = 20.sp)
                }
                Text(text = quantity.toString(), fontSize = 18.sp)
                IconButton(
                    onClick = {
                        incrementPressed = true
                        onIncrement()
                    },
                    modifier = Modifier.graphicsLayer(scaleX = incrementScale, scaleY = incrementScale)
                ) {
                    Text("+", fontSize = 20.sp)
                }
            }
        }
    }
}

