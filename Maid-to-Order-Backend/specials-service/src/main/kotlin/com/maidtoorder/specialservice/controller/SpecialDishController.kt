package com.maidtoorder.specialservice.controller

import com.maidtoorder.specialservice.dto.SpecialDishCreateDto
import com.maidtoorder.specialservice.dto.SpecialDishDto
import com.maidtoorder.specialservice.model.SpecialDishType
import com.maidtoorder.specialservice.service.SpecialDishService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/special-dishes")
@CrossOrigin(origins = ["*"])
class SpecialDishController(
    private val service: SpecialDishService
) {
    @GetMapping
    fun getAllSpecialDishes(
        @RequestParam(required = false) type: SpecialDishType?,
        @RequestParam(required = false, defaultValue = "false") today: Boolean
    ): ResponseEntity<List<SpecialDishDto>> {
        val dishes = when {
            today -> service.getToday()
            type != null -> service.getByType(type)
            else -> service.getAll()
        }
        return ResponseEntity.ok(dishes)
    }

    @GetMapping("/{id}")
    fun getSpecialDishById(@PathVariable id: Long): ResponseEntity<SpecialDishDto> {
        val dish = service.getSpecialDishById(id)
        return dish?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createSpecialDish(@Valid @RequestBody dto: SpecialDishCreateDto): ResponseEntity<SpecialDishDto> {
        val dish = service.create(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(dish)
    }

    @DeleteMapping("/{id}")
    fun deleteSpecialDish(@PathVariable id: Long): ResponseEntity<Void> {
        return if (service.delete(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
