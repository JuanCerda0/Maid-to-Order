package pkg.maid_to_order.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.draw.clip
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

    val context = LocalContext.current
    val dish = remember(dishId) { menuViewModel.getDishById(dishId) }

    // Animaciones de botones
    var addPressed by remember { mutableStateOf(false) }
    var sharePressed by remember { mutableStateOf(false) }

    val addScale by animateFloatAsState(
        targetValue = if (addPressed) 0.92f else 1f,
        animationSpec = tween(140), label = ""
    )

    val shareScale by animateFloatAsState(
        targetValue = if (sharePressed) 0.92f else 1f,
        animationSpec = tween(140), label = ""
    )

    fun shareDish() {
        dish?.let {
            val msg = """
                🍽️ ${it.name}

                ${it.description}

                Precio: $${String.format("%.0f", it.price)}
                Categoría: ${it.category}

                Disponible en Maid to Order.
            """.trimIndent()

            try {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    setPackage("com.whatsapp")
                    putExtra(Intent.EXTRA_TEXT, msg)
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                val genericIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, msg)
                }
                context.startActivity(Intent.createChooser(genericIntent, "Compartir"))
            }
        }
    }

    dish?.let { currentDish ->

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = currentDish.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.Close, contentDescription = "Volver")
                        }
                    },
                    actions = {
                        IconButton(
                            modifier = Modifier.graphicsLayer(
                                scaleX = shareScale,
                                scaleY = shareScale
                            ),
                            onClick = {
                                sharePressed = true
                                shareDish()
                            }
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Compartir")
                        }
                    }
                )
            }
        ) { padding ->

            Box(modifier = Modifier.fillMaxSize()) {

                // Fondo con alpha
                Image(
                    painter = painterResource(id = R.drawable.fondo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(alpha = 0.35f),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(20.dp)
                        .fillMaxSize()
                        .graphicsLayer(alpha = 1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Imagen principal con fallback + animación
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(300)),
                        exit = fadeOut()
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(currentDish.imageUri ?: currentDish.imageRes)
                                .crossfade(true)
                                .build(),
                            contentDescription = currentDish.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.logo_maid),
                            error = painterResource(R.drawable.logo_maid)
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = currentDish.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Categoría: ${currentDish.category}",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = currentDish.description,
                        fontSize = 17.sp,
                        lineHeight = 22.sp
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "$${String.format("%.0f", currentDish.price)}",
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(32.dp))

                    Button(
                        onClick = {
                            addPressed = true
                            cartViewModel.addToCart(currentDish)
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer(
                                scaleX = addScale,
                                scaleY = addScale
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Agregar al carrito", fontSize = 18.sp)
                    }
                }
            }
        }

    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Platillo no encontrado", fontSize = 18.sp)
        }
    }
}
