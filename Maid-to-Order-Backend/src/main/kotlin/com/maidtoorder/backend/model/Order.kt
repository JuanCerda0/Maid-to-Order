package com.maidtoorder.backend.model

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
    
    @Column(length = 100)
    val customerName: String? = null,
    
    @NotBlank(message = "El teléfono es obligatorio")
    @Column(nullable = false, length = 20)
    val customerPhone: String = "",
    
    @Column(length = 100)
    val customerEmail: String = "",
    
    @Column(length = 500)
    val deliveryAddress: String = "",
    
    @Column(length = 10)
    val tableNumber: String? = null,
    
    @Column(length = 1000)
    val notes: String? = null,
    
    @NotNull(message = "El total es obligatorio")
    @Column(nullable = false)
    val total: Double,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: OrderStatus = OrderStatus.PENDING,
    
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column
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
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_id", nullable = false)
    val dish: Dish,
    
    @NotNull(message = "La cantidad es obligatoria")
    @Column(nullable = false)
    val quantity: Int,
    
    @NotNull(message = "El subtotal es obligatorio")
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

