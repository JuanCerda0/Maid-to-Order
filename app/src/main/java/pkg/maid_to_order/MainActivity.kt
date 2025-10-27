package pkg.maid_to_order

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pkg.maid_to_order.data.Screen
import pkg.maid_to_order.ui.screens.CartScreen
import pkg.maid_to_order.ui.screens.DishDetailScreen
import pkg.maid_to_order.ui.screens.FormScreen
import pkg.maid_to_order.ui.screens.HomeScreen
import pkg.maid_to_order.ui.theme.MaidtoOrderTheme
import pkg.maid_to_order.viewmodel.CartViewModel
import pkg.maid_to_order.viewmodel.FormViewModel
import pkg.maid_to_order.viewmodel.MenuViewModel
import pkg.maid_to_order.ui.components.TopBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaidtoOrderTheme {
                MainApp()
            }
        }
    }
}



@Composable
fun MainApp() {

    val navController = rememberNavController()

    val menuViewModel: MenuViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()

    Scaffold() { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController, menuViewModel, cartViewModel)
            }

            composable("detail/{dishId}") { backStackEntry ->
                val dishId = backStackEntry.arguments?.getString("dishId")?.toInt() ?: 0
                DishDetailScreen(
                    navController = navController,
                    dishId = dishId,
                    menuViewModel = menuViewModel,
                    cartViewModel = cartViewModel
                )
            }

            composable(Screen.Cart.route) {
                CartScreen(
                    navController = navController,
                    cartViewModel = cartViewModel
                )
            }

            composable(Screen.Form.route) {
                val formViewModel: FormViewModel = viewModel()
                FormScreen(
                    navController = navController,
                    cartViewModel = cartViewModel,
                    formViewModel = formViewModel
                )
            }
        }
    }
}