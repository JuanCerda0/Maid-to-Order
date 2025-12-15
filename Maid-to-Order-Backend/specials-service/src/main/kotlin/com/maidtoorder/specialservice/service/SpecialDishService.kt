package com.maidtoorder.specialservice.service

import com.maidtoorder.specialservice.dto.SpecialDishCreateDto
import com.maidtoorder.specialservice.dto.SpecialDishDto
import com.maidtoorder.specialservice.model.SpecialDish
import com.maidtoorder.specialservice.model.SpecialDishType
import com.maidtoorder.specialservice.repository.SpecialDishRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class SpecialDishService(
    private val repository: SpecialDishRepository
) {
    fun getAll(): List<SpecialDishDto> =
        repository.findByAvailableTrue().map { SpecialDishDto.fromEntity(it) }

    fun getSpecialDishById(id: Long): SpecialDishDto? =
        repository.findById(id).map { SpecialDishDto.fromEntity(it) }.orElse(null)

    fun getByType(type: SpecialDishType): List<SpecialDishDto> =
        repository.findByTypeAndAvailableTrue(type).map { SpecialDishDto.fromEntity(it) }

    fun getToday(): List<SpecialDishDto> =
        repository.findByDateAndAvailableTrue(LocalDate.now()).map { SpecialDishDto.fromEntity(it) }

    fun create(dto: SpecialDishCreateDto): SpecialDishDto {
        val saved = repository.save(
            SpecialDish(
                name = dto.name,
                description = dto.description,
                price = dto.price,
                imageUrl = dto.imageUrl,
                type = dto.type,
                date = dto.date,
                available = dto.available
            )
        )
        return SpecialDishDto.fromEntity(saved)
    }

    fun delete(id: Long): Boolean =
        if (repository.existsById(id)) {
            repository.deleteById(id)
            true
        } else {
            false
        }
}
