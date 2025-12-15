package com.maidtoorder.backend.service

import com.maidtoorder.backend.dto.DishCreateDto
import com.maidtoorder.backend.dto.DishDto
import com.maidtoorder.backend.dto.DishUpdateDto
import com.maidtoorder.backend.model.Dish
import com.maidtoorder.backend.repository.DishRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DishService(
    private val dishRepository: DishRepository
) {
    
    fun getAllDishes(): List<DishDto> {
        return dishRepository.findAll().map { DishDto.fromEntity(it) }
    }
    
    fun getDishById(id: Long): DishDto? {
        return dishRepository.findById(id).map { DishDto.fromEntity(it) }.orElse(null)
    }
    
    fun getDishesByCategory(category: String): List<DishDto> {
        return dishRepository.findByCategory(category).map { DishDto.fromEntity(it) }
    }
    
    fun getAvailableDishes(): List<DishDto> {
        return dishRepository.findByAvailableTrue().map { DishDto.fromEntity(it) }
    }
    
    fun searchDishes(query: String): List<DishDto> {
        return dishRepository.findByNameContainingIgnoreCase(query).map { DishDto.fromEntity(it) }
    }
    
    fun createDish(dto: DishCreateDto): DishDto {
        val dish = Dish(
            name = dto.name,
            description = dto.description,
            price = dto.price,
            category = dto.category,
            imageUrl = dto.imageUrl,
            available = dto.available
        )
        val saved = dishRepository.save(dish)
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
        
        val saved = dishRepository.save(updated)
        return DishDto.fromEntity(saved)
    }
    
    fun deleteDish(id: Long): Boolean {
        return if (dishRepository.existsById(id)) {
            dishRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}

