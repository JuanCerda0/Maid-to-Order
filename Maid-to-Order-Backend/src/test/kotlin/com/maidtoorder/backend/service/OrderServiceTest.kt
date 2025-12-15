package com.maidtoorder.backend.service

import com.maidtoorder.backend.dto.OrderCreateDto
import com.maidtoorder.backend.dto.OrderItemCreateDto
import com.maidtoorder.backend.dto.OrderStatusUpdateDto
import com.maidtoorder.backend.model.Dish
import com.maidtoorder.backend.model.Order
import com.maidtoorder.backend.model.OrderStatus
import com.maidtoorder.backend.repository.DishRepository
import com.maidtoorder.backend.repository.OrderRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class OrderServiceTest {
    
    private lateinit var orderRepository: OrderRepository
    private lateinit var dishRepository: DishRepository
    private lateinit var orderService: OrderService
    
    @BeforeEach
    fun setUp() {
        orderRepository = mockk()
        dishRepository = mockk()
        orderService = OrderService(orderRepository, dishRepository)
    }
    
    @Test
    fun `should create order successfully`() {
        // Given
        val dish = Dish(id = 1, name = "Katsudon", description = "Test", price = 6700.0, category = "Test", available = true)
        val createDto = OrderCreateDto(
            items = listOf(OrderItemCreateDto(dishId = 1, quantity = 2)),
            customerPhone = "123456789"
        )
        val savedOrder = Order(
            id = 1,
            customerPhone = "123456789",
            total = 13400.0,
            status = OrderStatus.PENDING
        )
        
        every { dishRepository.findById(1) } returns Optional.of(dish)
        every { orderRepository.save(any()) } returns savedOrder
        
        // When
        val result = orderService.createOrder(createDto)
        
        // Then
        assertNotNull(result)
        assertEquals(13400.0, result.total)
        assertEquals(OrderStatus.PENDING, result.status)
        verify { dishRepository.findById(1) }
        verify { orderRepository.save(any()) }
    }
    
    @Test
    fun `should throw exception when dish not found`() {
        // Given
        val createDto = OrderCreateDto(
            items = listOf(OrderItemCreateDto(dishId = 999, quantity = 1)),
            customerPhone = "123456789"
        )
        
        every { dishRepository.findById(999) } returns Optional.empty()
        
        // When & Then
        assertThrows(IllegalArgumentException::class.java) {
            orderService.createOrder(createDto)
        }
    }
    
    @Test
    fun `should update order status`() {
        // Given
        val existingOrder = Order(
            id = 1,
            customerPhone = "123456789",
            total = 13400.0,
            status = OrderStatus.PENDING
        )
        val updateDto = OrderStatusUpdateDto(status = OrderStatus.CONFIRMED)
        val updatedOrder = existingOrder.copy(status = OrderStatus.CONFIRMED)
        
        every { orderRepository.findById(1) } returns Optional.of(existingOrder)
        every { orderRepository.save(any()) } returns updatedOrder
        
        // When
        val result = orderService.updateOrderStatus(1, updateDto)
        
        // Then
        assertNotNull(result)
        assertEquals(OrderStatus.CONFIRMED, result?.status)
        verify { orderRepository.findById(1) }
        verify { orderRepository.save(any()) }
    }
    
    @Test
    fun `should get all orders`() {
        // Given
        val orders = listOf(
            Order(id = 1, customerPhone = "123", total = 1000.0, status = OrderStatus.PENDING),
            Order(id = 2, customerPhone = "456", total = 2000.0, status = OrderStatus.CONFIRMED)
        )
        every { orderRepository.findAll() } returns orders
        
        // When
        val result = orderService.getAllOrders()
        
        // Then
        assertEquals(2, result.size)
        verify { orderRepository.findAll() }
    }
}

