package com.maidtoorder.orderservice.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableList<OrderItem> = mutableListOf(),

    val customerName: String? = null,

    @NotBlank
    @Column(nullable = false, length = 20)
    val customerPhone: String = "",

    val customerEmail: String = "",

    val deliveryAddress: String = "",

    val tableNumber: String? = null,

    val notes: String? = null,

    @NotNull
    @Column(nullable = false)
    val total: Double,

    @Enumerated(EnumType.STRING)
    val status: OrderStatus = OrderStatus.PENDING,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val updatedAt: LocalDateTime? = null
)

@Entity
@Table(name = "order_items")
data class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @Column(nullable = false)
    val dishId: Long,

    @Column(nullable = false, length = 200)
    val dishName: String,

    @Column(nullable = false)
    val quantity: Int,

    @Column(nullable = false)
    val subtotal: Double
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    IN_PREPARATION,
    READY_FOR_DELIVERY,
    IN_DELIVERY,
    DELIVERED,
    CANCELLED
}
