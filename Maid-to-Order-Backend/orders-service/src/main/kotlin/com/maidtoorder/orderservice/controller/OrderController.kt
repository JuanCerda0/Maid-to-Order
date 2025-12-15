package com.maidtoorder.orderservice.controller

import com.maidtoorder.orderservice.dto.OrderCreateDto
import com.maidtoorder.orderservice.dto.OrderDto
import com.maidtoorder.orderservice.dto.OrderStatusUpdateDto
import com.maidtoorder.orderservice.model.OrderStatus
import com.maidtoorder.orderservice.service.OrderService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
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
        return order?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createOrder(@Valid @RequestBody dto: OrderCreateDto): ResponseEntity<OrderDto> =
        try {
            ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(dto))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }

    @PutMapping("/{id}/status")
    fun updateOrderStatus(
        @PathVariable id: Long,
        @Valid @RequestBody dto: OrderStatusUpdateDto
    ): ResponseEntity<OrderDto> {
        val order = orderService.updateOrderStatus(id, dto)
        return order?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<Void> {
        return if (orderService.deleteOrder(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
