package pkg.maid_to_order.network.dto

import com.google.gson.annotations.SerializedName

data class OrderDto(
    @SerializedName("id") val id: Long,
    @SerializedName("items") val items: List<OrderItemDto>,
    @SerializedName("customerName") val customerName: String?,
    @SerializedName("customerPhone") val customerPhone: String,
    @SerializedName("customerEmail") val customerEmail: String,
    @SerializedName("deliveryAddress") val deliveryAddress: String,
    @SerializedName("tableNumber") val tableNumber: String?,
    @SerializedName("notes") val notes: String?,
    @SerializedName("total") val total: Double,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String?
)

data class OrderItemDto(
    @SerializedName("id") val id: Long,
    @SerializedName("dishId") val dishId: Long,
    @SerializedName("dishName") val dishName: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("subtotal") val subtotal: Double
)

data class OrderCreateDto(
    @SerializedName("items") val items: List<OrderItemCreateDto>,
    @SerializedName("customerName") val customerName: String? = null,
    @SerializedName("customerPhone") val customerPhone: String,
    @SerializedName("customerEmail") val customerEmail: String = "",
    @SerializedName("deliveryAddress") val deliveryAddress: String = "",
    @SerializedName("tableNumber") val tableNumber: String? = null,
    @SerializedName("notes") val notes: String? = null
)

data class OrderItemCreateDto(
    @SerializedName("dishId") val dishId: Long,
    @SerializedName("quantity") val quantity: Int
)

