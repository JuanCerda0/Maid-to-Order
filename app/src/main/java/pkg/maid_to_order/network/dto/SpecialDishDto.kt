package pkg.maid_to_order.network.dto

import com.google.gson.annotations.SerializedName

data class SpecialDishDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("type") val type: String,
    @SerializedName("date") val date: String,
    @SerializedName("available") val available: Boolean
)

