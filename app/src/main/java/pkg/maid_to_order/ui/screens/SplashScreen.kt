package pkg.maid_to_order.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import pkg.maid_to_order.R
import pkg.maid_to_order.data.Screen

@Composable
fun SplashScreen(
    navController: NavController
) {
    var visible by remember { mutableStateOf(false) }
    var scale by remember { mutableStateOf(0f) }

    // Animación de escala del logo
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(900),
        label = ""
    )

    LaunchedEffect(Unit) {
        // Inicia animaciones
        visible = true
        scale = 1f

        // Tiempo visible antes de navegar
        delay(1500)

        // Ir a Home y limpiar stack
        navController.navigate(Screen.Home.route) {
            popUpTo(0)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(700)),
            exit = fadeOut()
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
            ) {

                // Logo animado
                Image(
                    painter = painterResource(id = R.drawable.logo_maid),
                    contentDescription = "Logo Maid to Order",
                    modifier = Modifier
                        .size(220.dp)
                        .graphicsLayer(scaleX = animatedScale, scaleY = animatedScale)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Maid to Order",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sistema de Pedidos",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
