package pkg.maid_to_order.data.model

import java.util.UUID

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val items: List<OrderItem>,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String,
    val deliveryAddress: String,
    val total: Double,
    val status: OrderStatus = OrderStatus.PENDING
)

data class OrderItem(
    val dish: Dish,
    val quantity: Int,
    val subtotal: Double = dish.price * quantity
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    IN_PREPARATION,
    READY_FOR_DELIVERY,
    IN_DELIVERY,
    DELIVERED,
    CANCELLED
}