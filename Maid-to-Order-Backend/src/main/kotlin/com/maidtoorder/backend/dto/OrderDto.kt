package com.maidtoorder.backend.dto

import com.maidtoorder.backend.model.Order
import com.maidtoorder.backend.model.OrderStatus
import java.time.LocalDateTime

data class OrderDto(
    val id: Long,
    val items: List<OrderItemDto>,
    val customerName: String?,
    val customerPhone: String,
    val customerEmail: String,
    val deliveryAddress: String,
    val tableNumber: String?,
    val notes: String?,
    val total: Double,
    val status: OrderStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun fromEntity(order: Order): OrderDto {
            return OrderDto(
                id = order.id,
                items = order.items.map { OrderItemDto.fromEntity(it) },
                customerName = order.customerName,
                customerPhone = order.customerPhone,
                customerEmail = order.customerEmail,
                deliveryAddress = order.deliveryAddress,
                tableNumber = order.tableNumber,
                notes = order.notes,
                total = order.total,
                status = order.status,
                createdAt = order.createdAt,
                updatedAt = order.updatedAt
            )
        }
    }
}

data class OrderItemDto(
    val id: Long,
    val dishId: Long,
    val dishName: String,
    val quantity: Int,
    val subtotal: Double
) {
    companion object {
        fun fromEntity(item: com.maidtoorder.backend.model.OrderItem): OrderItemDto {
            return OrderItemDto(
                id = item.id,
                dishId = item.dish.id,
                dishName = item.dish.name,
                quantity = item.quantity,
                subtotal = item.subtotal
            )
        }
    }
}

data class OrderCreateDto(
    val items: List<OrderItemCreateDto>,
    val customerName: String? = null,
    val customerPhone: String,
    val customerEmail: String = "",
    val deliveryAddress: String = "",
    val tableNumber: String? = null,
    val notes: String? = null
)

data class OrderItemCreateDto(
    val dishId: Long,
    val quantity: Int
)

data class OrderStatusUpdateDto(
    val status: OrderStatus
)

