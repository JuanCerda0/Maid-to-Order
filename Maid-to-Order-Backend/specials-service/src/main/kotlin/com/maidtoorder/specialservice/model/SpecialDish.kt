package com.maidtoorder.specialservice.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

@Entity
@Table(name = "special_dishes")
data class SpecialDish(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotBlank
    val name: String,

    @NotBlank
    @Column(length = 500)
    val description: String,

    @NotNull
    val price: Double,

    @Column(length = 1000)
    val imageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    val type: SpecialDishType = SpecialDishType.CHEF_SPECIAL,

    val date: LocalDate = LocalDate.now(),

    val available: Boolean = true
)

enum class SpecialDishType {
    CHEF_SPECIAL,
    DAILY_SPECIAL,
    SEASONAL
}
