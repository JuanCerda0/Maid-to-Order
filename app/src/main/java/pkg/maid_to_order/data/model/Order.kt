package pkg.maid_to_order.data.model

import java.util.UUID

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val items: List<OrderItem>,
    val customerName: String,
    val tableNumber: String,
    val notes: String? = null,
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
    READY_TO_SERVE,
    SERVED,
    CANCELLED
}