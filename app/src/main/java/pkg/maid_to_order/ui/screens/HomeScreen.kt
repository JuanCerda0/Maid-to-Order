package pkg.maid_to_order.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import pkg.maid_to_order.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pkg.maid_to_order.data.Screen
import pkg.maid_to_order.data.model.Dish
import pkg.maid_to_order.ui.screens.SpecialDishCard
import pkg.maid_to_order.viewmodel.CartViewModel
import pkg.maid_to_order.viewmodel.MenuViewModel
import pkg.maid_to_order.viewmodel.WeatherUiState
import pkg.maid_to_order.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    menuViewModel: MenuViewModel,
    cartViewModel: CartViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>("Todos") }
    var isSearchActive by remember { mutableStateOf(false) }
    val weatherViewModel: WeatherViewModel = viewModel()
    val weatherState by weatherViewModel.uiState.collectAsState()

    val categories = menuViewModel.getCategories()
    val groupedMenu = menuViewModel.menuList.groupBy { it.category }
    val filteredDishes = if (isSearchActive && searchQuery.isNotEmpty()) {
        menuViewModel.searchDishes(searchQuery)
    } else {
        menuViewModel.getMenuByCategory(selectedCategory)
    }
    val specialDishes = menuViewModel.specialDishes
    val isGroupedView = !isSearchActive && (selectedCategory == null || selectedCategory == "Todos")
    
    // Animaciones
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )

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
                            modifier = Modifier.size(80.dp)
                        )
                        Text("Maid to Order - Menú", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Ver carrito"
                        )
                    }
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes"
                        )
                    }
                    IconButton(onClick = { navController.navigate(Screen.AdminLogin.route) }) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Admin"
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
            ) {
            // Barra de búsqueda
            SearchBar(
                query = searchQuery,
                onQueryChange = { 
                    searchQuery = it
                    isSearchActive = it.isNotEmpty()
                },
                onSearch = { isSearchActive = true },
                active = isSearchActive,
                onActiveChange = { isSearchActive = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                placeholder = { Text("Buscar platillos...") }
            ) {
                // Resultados de búsqueda en el dropdown
                if (searchQuery.isNotEmpty()) {
                    val searchResults = menuViewModel.searchDishes(searchQuery)
                    LazyColumn {
                        items(searchResults) { dish ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(Screen.DishDetail(dish.id).route)
                                        isSearchActive = false
                                    }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dish.name,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "$${String.format("%.0f", dish.price)}",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
            
            WeatherInfoCard(
                state = weatherState,
                onRetry = { weatherViewModel.refreshWeather() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
            
            // Filtros por categoría
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                            isSearchActive = false
                            searchQuery = ""
                        },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            
            // Sección de platos especiales
            if (specialDishes.isNotEmpty()) {
                AnimatedVisibility(
                    visible = specialDishes.isNotEmpty(),
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        Text(
                            text = "🌟 Especialidades",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(specialDishes) { dish ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn() + slideInHorizontally(),
                                    exit = fadeOut() + slideOutHorizontally()
                                ) {
                                    SpecialDishCard(
                                        dish = dish,
                                        onClick = {
                                            navController.navigate(Screen.DishDetail(dish.id).route)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Lista de platillos
            if (isGroupedView) {
                if (menuViewModel.menuList.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No hay platillos disponibles",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    MenuCategorySections(
                        modifier = Modifier.weight(1f),
                        groupedMenu = groupedMenu,
                        onDishClick = { dish ->
                            navController.navigate(Screen.DishDetail(dish.id).route)
                        }
                    )
                }
            } else if (filteredDishes.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isSearchActive) "No se encontraron platillos" else "No hay platillos en esta categoría",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                DishListColumn(
                    modifier = Modifier.weight(1f),
                    dishes = filteredDishes,
                    onDishSelected = { dishId ->
                        navController.navigate(Screen.DishDetail(dishId).route)
                    }
                )
            }
            }
        }
    }
}

@Composable
private fun MenuCategorySections(
    modifier: Modifier = Modifier,
    groupedMenu: Map<String, List<Dish>>,
    onDishClick: (Dish) -> Unit
) {
    val orderedGroups = groupedMenu.entries
        .filter { it.value.isNotEmpty() }
        .sortedBy { it.key }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = orderedGroups,
            key = { it.key }
        ) { (category, dishes) ->
            CategoryCarouselSection(
                category = category,
                dishes = dishes,
                onDishClick = onDishClick
            )
        }
    }
}

@Composable
private fun DishListColumn(
    modifier: Modifier = Modifier,
    dishes: List<Dish>,
    onDishSelected: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = dishes,
            key = { it.id }
        ) { dish ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
                exit = fadeOut() + slideOutVertically()
            ) {
                DishCard(
                    dish = dish,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onDishSelected(dish.id) }
                )
            }
        }
    }
}

@Composable
private fun CategoryCarouselSection(
    category: String,
    dishes: List<Dish>,
    onDishClick: (Dish) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = category,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = dishes,
                key = { it.id }
            ) { dish ->
                DishCard(
                    dish = dish,
                    modifier = Modifier.width(240.dp),
                    onClick = { onDishClick(dish) }
                )
            }
        }
    }
}

@Composable
fun WeatherInfoCard(
    state: WeatherUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        when (state) {
            WeatherUiState.Loading -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Text(text = "Obteniendo clima...", fontWeight = FontWeight.Medium)
                }
            }
            is WeatherUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Clima actual",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Temperatura: ${String.format("%.1f", state.temperature)}°C")
                    Text(text = "Viento: ${String.format("%.1f", state.windSpeed)} km/h")
                    Text(
                        text = "Actualizado: ${state.time}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            is WeatherUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "No pudimos obtener el clima",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(text = state.message)
                    TextButton(onClick = onRetry) {
                        Text("Reintentar")
                    }
                }
            }
        }
    }
}

@Composable
fun DishCard(
    dish: Dish,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    Card(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable { 
                isPressed = true
                onClick()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            // Imagen del platillo
            when {
                !dish.imageUri.isNullOrBlank() -> {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(dish.imageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = dish.name,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 12.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                dish.imageRes != null && dish.imageRes != 0 -> {
                    Image(
                        painter = painterResource(id = dish.imageRes),
                        contentDescription = dish.name,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 12.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    // Placeholder si no hay imagen
                    Card(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Sin imagen", fontSize = 12.sp)
                        }
                    }
                }
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dish.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dish.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${String.format("%.0f", dish.price)}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
