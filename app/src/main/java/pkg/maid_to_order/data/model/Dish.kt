package pkg.maid_to_order.data.model

data class Dish(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double, // Lo voy a arreglar
    val imageRes: Int // ID del recurso drawable (imagen del plato)
)