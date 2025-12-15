package com.maidtoorder.backend.service

import com.maidtoorder.backend.dto.*
import com.maidtoorder.backend.model.Order
import com.maidtoorder.backend.model.OrderItem
import com.maidtoorder.backend.model.OrderStatus
import com.maidtoorder.backend.repository.DishRepository
import com.maidtoorder.backend.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
    private val dishRepository: DishRepository
) {
    
    fun getAllOrders(): List<OrderDto> {
        return orderRepository.findAll().map { OrderDto.fromEntity(it) }
    }
    
    fun getOrderById(id: Long): OrderDto? {
        return orderRepository.findById(id).map { OrderDto.fromEntity(it) }.orElse(null)
    }
    
    fun getOrdersByStatus(status: OrderStatus): List<OrderDto> {
        return orderRepository.findByStatus(status).map { OrderDto.fromEntity(it) }
    }
    
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
            val dish = dishRepository.findById(itemDto.dishId)
                .orElseThrow { IllegalArgumentException("Plato con ID ${itemDto.dishId} no encontrado") }
            
            if (!dish.available) {
                throw IllegalArgumentException("El plato ${dish.name} no está disponible")
            }
            
            val subtotal = dish.price * itemDto.quantity
            total += subtotal
            
            val orderItem = OrderItem(
                order = order,
                dish = dish,
                quantity = itemDto.quantity,
                subtotal = subtotal
            )
            order.items.add(orderItem)
        }
        
        val savedOrder = order.copy(total = total)
        val saved = orderRepository.save(savedOrder)
        return OrderDto.fromEntity(saved)
    }
    
    fun updateOrderStatus(id: Long, dto: OrderStatusUpdateDto): OrderDto? {
        val order = orderRepository.findById(id).orElse(null) ?: return null
        
        val updated = order.copy(
            status = dto.status,
            updatedAt = LocalDateTime.now()
        )
        
        val saved = orderRepository.save(updated)
        return OrderDto.fromEntity(saved)
    }
    
    fun deleteOrder(id: Long): Boolean {
        return if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}

