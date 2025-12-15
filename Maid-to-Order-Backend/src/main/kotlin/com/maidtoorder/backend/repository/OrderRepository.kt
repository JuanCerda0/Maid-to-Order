package com.maidtoorder.backend.repository

import com.maidtoorder.backend.model.Order
import com.maidtoorder.backend.model.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun findByStatus(status: OrderStatus): List<Order>
    fun findByCustomerPhone(phone: String): List<Order>
    fun findByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): List<Order>
}

