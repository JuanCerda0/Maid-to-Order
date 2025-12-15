package com.maidtoorder.orderservice.dto

import com.maidtoorder.orderservice.model.Order
import com.maidtoorder.orderservice.model.OrderItem
import com.maidtoorder.orderservice.model.OrderStatus
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
        fun fromEntity(order: Order) = OrderDto(
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

data class OrderItemDto(
    val id: Long,
    val dishId: Long,
    val dishName: String,
    val quantity: Int,
    val subtotal: Double
) {
    companion object {
        fun fromEntity(item: OrderItem) = OrderItemDto(
            id = item.id,
            dishId = item.dishId,
            dishName = item.dishName,
            quantity = item.quantity,
            subtotal = item.subtotal
        )
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

data class DishSummaryDto(
    val id: Long,
    val name: String,
    val price: Double
)
