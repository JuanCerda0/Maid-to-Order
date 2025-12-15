package pkg.maid_to_order.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pkg.maid_to_order.R
import pkg.maid_to_order.viewmodel.CartViewModel
import pkg.maid_to_order.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDetailScreen(
    navController: NavController,
    dishId: Int,
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel
) {
    val dish = menuViewModel.getDishById(dishId)
    val context = LocalContext.current
    var addToCartPressed by remember { mutableStateOf(false) }
    var sharePressed by remember { mutableStateOf(false) }

    val addToCartScale by animateFloatAsState(
        targetValue = if (addToCartPressed) 0.9f else 1f,
        animationSpec = tween(150), label = "addToCartScale"
    )

    val shareScale by animateFloatAsState(
        targetValue = if (sharePressed) 0.9f else 1f,
        animationSpec = tween(150), label = "shareScale"
    )

    fun shareViaWhatsApp(dish: pkg.maid_to_order.data.model.Dish) {
        val message = """
            🍽️ ${dish.name}
            
            ${dish.description}
            
            Precio: $${String.format("%.0f", dish.price)}
            Categoría: ${dish.category}
            
            ¡Delicioso platillo disponible en Maid to Order!
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            setPackage("com.whatsapp")
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Si WhatsApp no está instalado, usar intent genérico
            val genericIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            context.startActivity(Intent.createChooser(genericIntent, "Compartir por"))
        }
    }

    dish?.let { currentDish ->
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
                            Text(currentDish.name, fontWeight = FontWeight.Bold)
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
                    actions = {
                        IconButton(
                            onClick = {
                                sharePressed = true
                                shareViaWhatsApp(currentDish)
                            },
                            modifier = Modifier
                                .graphicsLayer(scaleX = shareScale, scaleY = shareScale)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartir"
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
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                // Imagen del platillo
                if (currentDish.imageUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(currentDish.imageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = currentDish.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                } else if (currentDish.imageRes != null) {
                    Image(
                        painter = painterResource(id = currentDish.imageRes),
                        contentDescription = currentDish.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentDish.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Categoría: ${currentDish.category}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentDish.description,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "$${String.format("%.0f", currentDish.price)}",
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        addToCartPressed = true
                        cartViewModel.addToCart(currentDish)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(scaleX = addToCartScale, scaleY = addToCartScale),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Agregar al Carrito", fontSize = 18.sp)
                }
                }
            }
        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Plato no encontrado", fontSize = 18.sp)
        }
    }
}

