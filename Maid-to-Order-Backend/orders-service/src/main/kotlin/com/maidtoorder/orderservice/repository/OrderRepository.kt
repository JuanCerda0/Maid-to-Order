package com.maidtoorder.orderservice.repository

import com.maidtoorder.orderservice.model.Order
import com.maidtoorder.orderservice.model.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun findByStatus(status: OrderStatus): List<Order>
}
