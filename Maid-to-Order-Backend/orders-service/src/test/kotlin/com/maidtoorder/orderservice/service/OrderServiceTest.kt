package com.maidtoorder.orderservice.service

import com.maidtoorder.orderservice.dto.DishSummaryDto
import com.maidtoorder.orderservice.dto.OrderCreateDto
import com.maidtoorder.orderservice.dto.OrderItemCreateDto
import com.maidtoorder.orderservice.dto.OrderStatusUpdateDto
import com.maidtoorder.orderservice.model.Order
import com.maidtoorder.orderservice.model.OrderStatus
import com.maidtoorder.orderservice.repository.OrderRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestTemplate
import java.util.Optional

class OrderServiceTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var restTemplate: RestTemplate
    private lateinit var orderService: OrderService

    @BeforeEach
    fun setup() {
        orderRepository = mockk()
        restTemplate = mockk()
        orderService = OrderService(orderRepository, restTemplate)
    }

    @Test
    fun `should create order calculating totals`() {
        val createDto = OrderCreateDto(
            items = listOf(OrderItemCreateDto(dishId = 1, quantity = 2)),
            customerPhone = "123456789"
        )
        val savedOrder = Order(
            id = 10,
            customerPhone = "123456789",
            total = 20000.0,
            status = OrderStatus.PENDING
        )

        every { restTemplate.getForObject(any<String>(), DishSummaryDto::class.java) } returns
            DishSummaryDto(id = 1, name = "Ramen", price = 10000.0)
        every { orderRepository.save(any()) } returns savedOrder

        val result = orderService.createOrder(createDto)

        assertEquals(10, result.id)
        assertEquals(20000.0, result.total)
        verify { orderRepository.save(any()) }
    }

    @Test
    fun `should update status`() {
        val existingOrder = Order(
            id = 5,
            customerPhone = "222",
            total = 5000.0,
            status = OrderStatus.PENDING
        )
        every { orderRepository.findById(5) } returns Optional.of(existingOrder)
        every { orderRepository.save(any()) } answers { firstArg() }

        val result = orderService.updateOrderStatus(5, OrderStatusUpdateDto(OrderStatus.CONFIRMED))

        assertEquals(OrderStatus.CONFIRMED, result?.status)
        assertNotNull(result?.updatedAt)
    }
}
