package pkg.maid_to_order.network.dto

import com.google.gson.annotations.SerializedName

data class DishDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("category") val category: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("available") val available: Boolean
)

data class DishCreateDto(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("category") val category: String = "General",
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("available") val available: Boolean = true
)

