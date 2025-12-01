package pkg.maid_to_order.data.model

data class FoodItem (
    val id: String,
    val name: String,
    val brand: String? = null,
    val description: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val servingSize: String
)