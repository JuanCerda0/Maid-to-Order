package com.maidtoorder.dishservice.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@Entity
@Table(name = "dishes")
data class Dish(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotBlank(message = "El nombre del plato es obligatorio")
    @Column(nullable = false)
    val name: String,

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, length = 500)
    val description: String,

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    @Column(nullable = false)
    val price: Double,

    @NotBlank(message = "La categoría es obligatoria")
    @Column(nullable = false)
    val category: String = "General",

    @Column(length = 1000)
    val imageUrl: String? = null,

    @Column(nullable = false)
    val available: Boolean = true
)
