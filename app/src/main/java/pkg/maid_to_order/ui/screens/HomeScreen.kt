package pkg.maid_to_order.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import pkg.maid_to_order.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pkg.maid_to_order.data.Screen
import pkg.maid_to_order.ui.theme.colorBarra
import pkg.maid_to_order.viewmodel.CartViewModel
import pkg.maid_to_order.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel
) {

    val dishes = menuViewModel.loadMenu()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(colorBarra)
                    .fillMaxWidth()
                    .padding(8.dp),
                title = { Text("Maid to Order", fontSize = 20.sp) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null) }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(dishes) { dish ->
                DishCard(
                    name = dish.name,
                    price = dish.price,
                    imageRes = dish.imageRes,
                    onClick = {
                        navController.navigate(Screen.DishDetail(dish.id).route)
                    }
                )
            }
        }

    }

}


@Composable
fun DishCard(
    name: String,
    price: Double,
    imageRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp)
            )
            Column {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "$${price}", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
