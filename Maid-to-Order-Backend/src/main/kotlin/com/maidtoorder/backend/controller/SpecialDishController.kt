package com.maidtoorder.backend.controller

import com.maidtoorder.backend.dto.SpecialDishCreateDto
import com.maidtoorder.backend.dto.SpecialDishDto
import com.maidtoorder.backend.model.SpecialDishType
import com.maidtoorder.backend.service.SpecialDishService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/special-dishes")
@CrossOrigin(origins = ["*"])
class SpecialDishController(
    private val specialDishService: SpecialDishService
) {
    
    @GetMapping
    fun getAllSpecialDishes(
        @RequestParam(required = false) type: SpecialDishType?,
        @RequestParam(required = false, defaultValue = "false") today: Boolean
    ): ResponseEntity<List<SpecialDishDto>> {
        val dishes = when {
            today -> specialDishService.getTodaySpecialDishes()
            type != null -> specialDishService.getSpecialDishesByType(type)
            else -> specialDishService.getAvailableSpecialDishes()
        }
        return ResponseEntity.ok(dishes)
    }
    
    @GetMapping("/{id}")
    fun getSpecialDishById(@PathVariable id: Long): ResponseEntity<SpecialDishDto> {
        val dish = specialDishService.getSpecialDishById(id)
        return if (dish != null) {
            ResponseEntity.ok(dish)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @PostMapping
    fun createSpecialDish(@Valid @RequestBody dto: SpecialDishCreateDto): ResponseEntity<SpecialDishDto> {
        val dish = specialDishService.createSpecialDish(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(dish)
    }
    
    @DeleteMapping("/{id}")
    fun deleteSpecialDish(@PathVariable id: Long): ResponseEntity<Void> {
        val deleted = specialDishService.deleteSpecialDish(id)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

