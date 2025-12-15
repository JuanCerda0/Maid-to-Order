package com.maidtoorder.orderservice.service

import com.maidtoorder.orderservice.dto.*
import com.maidtoorder.orderservice.model.Order
import com.maidtoorder.orderservice.model.OrderItem
import com.maidtoorder.orderservice.model.OrderStatus
import com.maidtoorder.orderservice.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
    private val restTemplate: RestTemplate
) {
    fun getAllOrders(): List<OrderDto> =
        orderRepository.findAll().map { OrderDto.fromEntity(it) }

    fun getOrderById(id: Long): OrderDto? =
        orderRepository.findById(id).map { OrderDto.fromEntity(it) }.orElse(null)

    fun getOrdersByStatus(status: OrderStatus): List<OrderDto> =
        orderRepository.findByStatus(status).map { OrderDto.fromEntity(it) }

    fun createOrder(dto: OrderCreateDto): OrderDto {
        val order = Order(
            customerName = dto.customerName,
            customerPhone = dto.customerPhone,
            customerEmail = dto.customerEmail,
            deliveryAddress = dto.deliveryAddress,
            tableNumber = dto.tableNumber,
            notes = dto.notes,
            total = 0.0,
            status = OrderStatus.PENDING
        )

        var total = 0.0
        dto.items.forEach { itemDto ->
            val dish = fetchDish(itemDto.dishId)
                ?: throw IllegalArgumentException("Plato con ID ${itemDto.dishId} no encontrado")

            val subtotal = dish.price * itemDto.quantity
            total += subtotal

            val orderItem = OrderItem(
                order = order,
                dishId = dish.id,
                dishName = dish.name,
                quantity = itemDto.quantity,
                subtotal = subtotal
            )
            order.items.add(orderItem)
        }

        val saved = orderRepository.save(order.copy(total = total))
        return OrderDto.fromEntity(saved)
    }

    fun updateOrderStatus(id: Long, dto: OrderStatusUpdateDto): OrderDto? {
        val order = orderRepository.findById(id).orElse(null) ?: return null
        val updated = order.copy(
            status = dto.status,
            updatedAt = LocalDateTime.now()
        )
        return OrderDto.fromEntity(orderRepository.save(updated))
    }

    fun deleteOrder(id: Long): Boolean =
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id)
            true
        } else false

    private fun fetchDish(id: Long): DishSummaryDto? =
        restTemplate.getForObject(
            "http://localhost:8081/api/dishes/$id",
            DishSummaryDto::class.java
        )
}
