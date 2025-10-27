package pkg.maid_to_order.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pkg.maid_to_order.data.Screen

@Composable
fun TopBar(navController: NavController) {

    val colorBarra = Color(0xFF2196F3)

    var searchText by remember { mutableStateOf("") }

    Row(

        modifier = Modifier

            .fillMaxWidth()
            .background(colorBarra)
            .padding(8.dp),

        verticalAlignment = Alignment.CenterVertically,

        horizontalArrangement = Arrangement.SpaceBetween

    ) {
       TextField(

            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Buscar...") },
            trailingIcon = {

                IconButton(onClick = { searchText = "" }) {

                    Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda")

                }
            },
            modifier = Modifier

                .width(520.dp)
                .height(50.dp),

            singleLine = true,

            colors = TextFieldDefaults.colors(

                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Sección derecha
        Row {
            IconButton(onClick = { /* perfil usuario */ }) {
                Icon(Icons.Default.AccountBox, contentDescription = null) }

            IconButton(onClick = { (Screen.Cart.route)}) { Icon(Icons.Default.ShoppingCart, contentDescription = null) }
        }
    }
}
