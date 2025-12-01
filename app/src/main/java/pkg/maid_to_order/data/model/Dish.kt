package pkg.maid_to_order.data.model

data class Dish(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String = "General",
    val imageRes: Int? = null, // ID del recurso drawable (imagen del plato)
    val imageUri: String? = null // URI de imagen tomada con cámara
)