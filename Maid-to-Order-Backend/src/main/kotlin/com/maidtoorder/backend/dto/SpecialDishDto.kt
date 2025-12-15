package com.maidtoorder.backend.dto

import com.maidtoorder.backend.model.SpecialDish
import com.maidtoorder.backend.model.SpecialDishType
import java.time.LocalDate

data class SpecialDishDto(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String?,
    val type: SpecialDishType,
    val date: LocalDate,
    val available: Boolean
) {
    companion object {
        fun fromEntity(specialDish: SpecialDish): SpecialDishDto {
            return SpecialDishDto(
                id = specialDish.id,
                name = specialDish.name,
                description = specialDish.description,
                price = specialDish.price,
                imageUrl = specialDish.imageUrl,
                type = specialDish.type,
                date = specialDish.date,
                available = specialDish.available
            )
        }
    }
}

data class SpecialDishCreateDto(
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String? = null,
    val type: SpecialDishType = SpecialDishType.CHEF_SPECIAL,
    val date: LocalDate = LocalDate.now(),
    val available: Boolean = true
)

