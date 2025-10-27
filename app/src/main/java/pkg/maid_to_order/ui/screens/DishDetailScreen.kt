package pkg.maid_to_order.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pkg.maid_to_order.viewmodel.CartViewModel
import pkg.maid_to_order.viewmodel.MenuViewModel
import pkg.maid_to_order.data.model.Dish

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDetailScreen(
    navController: NavController,
    dishId: Int,
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel
) {
    val dish = menuViewModel.getDishById(dishId)

    dish?.let { currentDish ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentDish.name) },
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = currentDish.imageRes),
                    contentDescription = currentDish.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentDish.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentDish.description,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$${currentDish.price}",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        cartViewModel.addToCart(currentDish)
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar al Carrito")
                }
            }
        }
    } ?: run {
        Text("Plato no encontrado")
    }
}

