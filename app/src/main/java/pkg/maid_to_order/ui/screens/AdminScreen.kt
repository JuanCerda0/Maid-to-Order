package pkg.maid_to_order.ui.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File
import pkg.maid_to_order.R
import pkg.maid_to_order.data.model.Dish
import pkg.maid_to_order.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    navController: NavController,
    menuViewModel: MenuViewModel
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingDish by remember { mutableStateOf<Dish?>(null) }

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
                        Text("Panel Administrativo", fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar platillo")
            }
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
            val dishes = menuViewModel.loadMenu()

            if (dishes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "No hay platillos",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Presiona el botón + para agregar uno",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                items(dishes) { dish ->
                    AdminDishCard(
                        dish = dish,
                        onEdit = { editingDish = dish },
                        onDelete = { menuViewModel.deleteDish(dish.id) }
                    )
                }
            }
        }

        if (showAddDialog || editingDish != null) {
            DishFormDialog(
                dish = editingDish,
                onDismiss = {
                    showAddDialog = false
                    editingDish = null
                },
                onSave = { dish ->
                    if (editingDish != null) {
                        menuViewModel.updateDish(dish)
                    } else {
                        menuViewModel.addDish(dish)
                    }
                    showAddDialog = false
                    editingDish = null
                }
            )
            }
        }
    }
}

@Composable
fun AdminDishCard(
    dish: Dish,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var deletePressed by remember { mutableStateOf(false) }
    var editPressed by remember { mutableStateOf(false) }

    val deleteScale by animateFloatAsState(
        targetValue = if (deletePressed) 0.85f else 1f,
        animationSpec = tween(150), label = "deleteScale"
    )

    val editScale by animateFloatAsState(
        targetValue = if (editPressed) 0.85f else 1f,
        animationSpec = tween(150), label = "editScale"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen
            if (dish.imageUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(dish.imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = dish.name,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            } else if (dish.imageRes != null) {
                Image(
                    painter = painterResource(id = dish.imageRes),
                    contentDescription = dish.name,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dish.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dish.category,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "$${String.format("%.0f", dish.price)}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(
                onClick = {
                    editPressed = true
                    onEdit()
                },
                modifier = Modifier.graphicsLayer(scaleX = editScale, scaleY = editScale)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }

            IconButton(
                onClick = {
                    deletePressed = true
                    onDelete()
                },
                modifier = Modifier.graphicsLayer(scaleX = deleteScale, scaleY = deleteScale)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishFormDialog(
    dish: Dish?,
    onDismiss: () -> Unit,
    onSave: (Dish) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(dish?.name ?: "") }
    var description by remember { mutableStateOf(dish?.description ?: "") }
    var price by remember { mutableStateOf(dish?.price?.toString() ?: "") }
    var category by remember { mutableStateOf(dish?.category ?: "General") }
    var imageUri by remember { mutableStateOf<Uri?>(dish?.imageUri?.let { Uri.parse(it) }) }
    var imageRes by remember { mutableStateOf<Int?>(dish?.imageRes) }
    var photoFile by remember { mutableStateOf<File?>(null) }

    val categories = listOf("General", "Platos Principales", "Sopas", "Aperitivos", "Bebidas", "Postres")

    // Launcher para seleccionar imagen de galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            imageRes = null
            photoFile = null
        }
    }

    // Launcher para tomar foto con cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoFile != null) {
            val uri = pkg.maid_to_order.utils.CameraUtils.getImageUri(context, photoFile!!)
            imageUri = uri
            imageRes = null
        }
    }

    fun openCameraInternal() {
        val file = pkg.maid_to_order.utils.CameraUtils.createImageFile(context)
        photoFile = file
        val uri = pkg.maid_to_order.utils.CameraUtils.getImageUri(context, file)
        cameraLauncher.launch(uri)
    }

    // Launcher para solicitar permisos de cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCameraInternal()
        }
    }

    fun openCamera() {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (dish != null) "Editar Platillo" else "Nuevo Platillo",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Selector de categoría
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Selector de imagen
                Text(
                    text = "Imagen del Platillo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var galleryPressed by remember { mutableStateOf(false) }
                    var cameraPressed by remember { mutableStateOf(false) }
                    
                    val galleryScale by animateFloatAsState(
                        targetValue = if (galleryPressed) 0.9f else 1f,
                        animationSpec = tween(150), label = "galleryScale"
                    )
                    
                    val cameraScale by animateFloatAsState(
                        targetValue = if (cameraPressed) 0.9f else 1f,
                        animationSpec = tween(150), label = "cameraScale"
                    )
                    
                    OutlinedButton(
                        onClick = {
                            galleryPressed = true
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .graphicsLayer(scaleX = galleryScale, scaleY = galleryScale),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("📷 Galería")
                    }
                    Button(
                        onClick = {
                            cameraPressed = true
                            openCamera()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .graphicsLayer(scaleX = cameraScale, scaleY = cameraScale),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("📸 Cámara")
                    }
                }

                // Vista previa de imagen
                if (imageUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Vista previa",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                } else if (imageRes != null) {
                    Image(
                        painter = painterResource(id = imageRes!!),
                        contentDescription = "Vista previa",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            val priceValue = price.toDoubleOrNull() ?: 0.0
                            val newDish = Dish(
                                id = dish?.id ?: 0,
                                name = name,
                                description = description,
                                price = priceValue,
                                category = category,
                                imageRes = imageRes,
                                imageUri = imageUri?.toString()
                            )
                            onSave(newDish)
                        },
                        modifier = Modifier.weight(1f),
                        enabled = name.isNotBlank() && price.toDoubleOrNull() != null
                    ) {
                        Text(if (dish != null) "Guardar" else "Agregar")
                    }
                }
            }
        }
    }
}

