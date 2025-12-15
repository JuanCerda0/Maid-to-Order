package com.maidtoorder.backend.controller

import com.maidtoorder.backend.dto.OrderCreateDto
import com.maidtoorder.backend.dto.OrderDto
import com.maidtoorder.backend.dto.OrderStatusUpdateDto
import com.maidtoorder.backend.model.OrderStatus
import com.maidtoorder.backend.service.OrderService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = ["*"])
class OrderController(
    private val orderService: OrderService
) {
    
    @GetMapping
    fun getAllOrders(
        @RequestParam(required = false) status: OrderStatus?
    ): ResponseEntity<List<OrderDto>> {
        val orders = if (status != null) {
            orderService.getOrdersByStatus(status)
        } else {
            orderService.getAllOrders()
        }
        return ResponseEntity.ok(orders)
    }
    
    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long): ResponseEntity<OrderDto> {
        val order = orderService.getOrderById(id)
        return if (order != null) {
            ResponseEntity.ok(order)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @PostMapping
    fun createOrder(@Valid @RequestBody dto: OrderCreateDto): ResponseEntity<OrderDto> {
        return try {
            val order = orderService.createOrder(dto)
            ResponseEntity.status(HttpStatus.CREATED).body(order)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
    
    @PutMapping("/{id}/status")
    fun updateOrderStatus(
        @PathVariable id: Long,
        @Valid @RequestBody dto: OrderStatusUpdateDto
    ): ResponseEntity<OrderDto> {
        val order = orderService.updateOrderStatus(id, dto)
        return if (order != null) {
            ResponseEntity.ok(order)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<Void> {
        val deleted = orderService.deleteOrder(id)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

