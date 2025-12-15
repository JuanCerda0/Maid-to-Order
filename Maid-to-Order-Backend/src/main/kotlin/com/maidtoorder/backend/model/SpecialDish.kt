package com.maidtoorder.backend.model

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
    
    @NotBlank(message = "El nombre del plato especial es obligatorio")
    @Column(nullable = false)
    val name: String,
    
    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, length = 500)
    val description: String,
    
    @NotNull(message = "El precio es obligatorio")
    @Column(nullable = false)
    val price: Double,
    
    @Column(length = 1000)
    val imageUrl: String? = null,
    
    @Column(nullable = false)
    val type: SpecialDishType = SpecialDishType.CHEF_SPECIAL,
    
    @Column(nullable = false)
    val date: LocalDate = LocalDate.now(),
    
    @Column(nullable = false)
    val available: Boolean = true
)

enum class SpecialDishType {
    CHEF_SPECIAL,      // Producto de autor
    DAILY_SPECIAL,     // Especialidad del día
    SEASONAL           // Especialidad de temporada
}

