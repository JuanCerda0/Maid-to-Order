package com.maidtoorder.specialservice.dto

import com.maidtoorder.specialservice.model.SpecialDish
import com.maidtoorder.specialservice.model.SpecialDishType
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
        fun fromEntity(entity: SpecialDish) = SpecialDishDto(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            price = entity.price,
            imageUrl = entity.imageUrl,
            type = entity.type,
            date = entity.date,
            available = entity.available
        )
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
