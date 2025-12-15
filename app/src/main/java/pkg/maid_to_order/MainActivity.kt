package pkg.maid_to_order

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import pkg.maid_to_order.data.Screen
import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import pkg.maid_to_order.ui.screens.AdminLoginScreen
import pkg.maid_to_order.ui.screens.AdminScreen
import pkg.maid_to_order.ui.screens.CartScreen
import pkg.maid_to_order.ui.screens.DishDetailScreen
import pkg.maid_to_order.ui.screens.FormScreen
import pkg.maid_to_order.ui.screens.HomeScreen
import pkg.maid_to_order.ui.screens.SettingsScreen
import pkg.maid_to_order.ui.theme.MaidtoOrderTheme
import pkg.maid_to_order.viewmodel.CartViewModel
import pkg.maid_to_order.viewmodel.FormViewModel
import pkg.maid_to_order.viewmodel.MenuViewModel
import pkg.maid_to_order.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
            
            MaidtoOrderTheme(darkTheme = isDarkMode) {
                MainApp(settingsViewModel)
            }
        }
    }
}

@Composable
fun MainApp(settingsViewModel: SettingsViewModel) {

    val navController = rememberNavController()

    val menuViewModel: MenuViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()

    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { -it }
                )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { -it }
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { it }
                )
            }
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

            composable(Screen.Settings.route) {
                SettingsScreen(
                    navController = navController,
                    settingsViewModel = settingsViewModel
                )
            }

            composable(Screen.AdminLogin.route) {
                AdminLoginScreen(navController = navController)
            }

            composable(Screen.Admin.route) {
                AdminScreen(
                    navController = navController,
                    menuViewModel = menuViewModel
                )
            }
        }
    }
}



