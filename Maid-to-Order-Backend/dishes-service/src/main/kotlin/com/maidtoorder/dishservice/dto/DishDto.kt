package com.maidtoorder.dishservice.dto

import com.maidtoorder.dishservice.model.Dish

data class DishDto(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String?,
    val available: Boolean
) {
    companion object {
        fun fromEntity(dish: Dish) = DishDto(
            id = dish.id,
            name = dish.name,
            description = dish.description,
            price = dish.price,
            category = dish.category,
            imageUrl = dish.imageUrl,
            available = dish.available
        )
    }
}

data class DishCreateDto(
    val name: String,
    val description: String,
    val price: Double,
    val category: String = "General",
    val imageUrl: String? = null,
    val available: Boolean = true
)

data class DishUpdateDto(
    val name: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val category: String? = null,
    val imageUrl: String? = null,
    val available: Boolean? = null
)
