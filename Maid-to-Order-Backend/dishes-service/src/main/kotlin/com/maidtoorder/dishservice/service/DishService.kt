package com.maidtoorder.dishservice.service

import com.maidtoorder.dishservice.dto.DishCreateDto
import com.maidtoorder.dishservice.dto.DishDto
import com.maidtoorder.dishservice.dto.DishUpdateDto
import com.maidtoorder.dishservice.model.Dish
import com.maidtoorder.dishservice.repository.DishRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DishService(
    private val dishRepository: DishRepository
) {
    fun getAllDishes(): List<DishDto> =
        dishRepository.findAll().map { DishDto.fromEntity(it) }

    fun getDishById(id: Long): DishDto? =
        dishRepository.findById(id).map { DishDto.fromEntity(it) }.orElse(null)

    fun getDishesByCategory(category: String): List<DishDto> =
        dishRepository.findByCategory(category).map { DishDto.fromEntity(it) }

    fun getAvailableDishes(): List<DishDto> =
        dishRepository.findByAvailableTrue().map { DishDto.fromEntity(it) }

    fun searchDishes(query: String): List<DishDto> =
        dishRepository.findByNameContainingIgnoreCase(query).map { DishDto.fromEntity(it) }

    fun createDish(dto: DishCreateDto): DishDto {
        val saved = dishRepository.save(
            Dish(
                name = dto.name,
                description = dto.description,
                price = dto.price,
                category = dto.category,
                imageUrl = dto.imageUrl,
                available = dto.available
            )
        )
        return DishDto.fromEntity(saved)
    }

    fun updateDish(id: Long, dto: DishUpdateDto): DishDto? {
        val dish = dishRepository.findById(id).orElse(null) ?: return null
        val updated = dish.copy(
            name = dto.name ?: dish.name,
            description = dto.description ?: dish.description,
            price = dto.price ?: dish.price,
            category = dto.category ?: dish.category,
            imageUrl = dto.imageUrl ?: dish.imageUrl,
            available = dto.available ?: dish.available
        )
        return DishDto.fromEntity(dishRepository.save(updated))
    }

    fun deleteDish(id: Long): Boolean =
        if (dishRepository.existsById(id)) {
            dishRepository.deleteById(id)
            true
        } else {
            false
        }
}
