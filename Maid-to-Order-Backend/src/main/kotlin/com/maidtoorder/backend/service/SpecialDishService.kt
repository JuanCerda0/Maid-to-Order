package com.maidtoorder.backend.service

import com.maidtoorder.backend.dto.SpecialDishCreateDto
import com.maidtoorder.backend.dto.SpecialDishDto
import com.maidtoorder.backend.model.SpecialDish
import com.maidtoorder.backend.model.SpecialDishType
import com.maidtoorder.backend.repository.SpecialDishRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class SpecialDishService(
    private val specialDishRepository: SpecialDishRepository
) {
    
    fun getAllSpecialDishes(): List<SpecialDishDto> {
        return specialDishRepository.findAll().map { SpecialDishDto.fromEntity(it) }
    }
    
    fun getSpecialDishById(id: Long): SpecialDishDto? {
        return specialDishRepository.findById(id).map { SpecialDishDto.fromEntity(it) }.orElse(null)
    }
    
    fun getSpecialDishesByType(type: SpecialDishType): List<SpecialDishDto> {
        return specialDishRepository.findByTypeAndAvailableTrue(type).map { SpecialDishDto.fromEntity(it) }
    }
    
    fun getTodaySpecialDishes(): List<SpecialDishDto> {
        val today = LocalDate.now()
        return specialDishRepository.findByDateAndAvailableTrue(today).map { SpecialDishDto.fromEntity(it) }
    }
    
    fun getAvailableSpecialDishes(): List<SpecialDishDto> {
        return specialDishRepository.findByAvailableTrue().map { SpecialDishDto.fromEntity(it) }
    }
    
    fun createSpecialDish(dto: SpecialDishCreateDto): SpecialDishDto {
        val specialDish = SpecialDish(
            name = dto.name,
            description = dto.description,
            price = dto.price,
            imageUrl = dto.imageUrl,
            type = dto.type,
            date = dto.date,
            available = dto.available
        )
        val saved = specialDishRepository.save(specialDish)
        return SpecialDishDto.fromEntity(saved)
    }
    
    fun deleteSpecialDish(id: Long): Boolean {
        return if (specialDishRepository.existsById(id)) {
            specialDishRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}

