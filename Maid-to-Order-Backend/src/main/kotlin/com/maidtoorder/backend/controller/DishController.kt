package com.maidtoorder.backend.controller

import com.maidtoorder.backend.dto.DishCreateDto
import com.maidtoorder.backend.dto.DishDto
import com.maidtoorder.backend.dto.DishUpdateDto
import com.maidtoorder.backend.service.DishService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dishes")
@CrossOrigin(origins = ["*"])
class DishController(
    private val dishService: DishService
) {
    
    @GetMapping
    fun getAllDishes(
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) available: Boolean?,
        @RequestParam(required = false) search: String?
    ): ResponseEntity<List<DishDto>> {
        val dishes = when {
            search != null -> dishService.searchDishes(search)
            category != null && category != "Todos" -> dishService.getDishesByCategory(category)
            available == true -> dishService.getAvailableDishes()
            else -> dishService.getAllDishes()
        }
        return ResponseEntity.ok(dishes)
    }
    
    @GetMapping("/{id}")
    fun getDishById(@PathVariable id: Long): ResponseEntity<DishDto> {
        val dish = dishService.getDishById(id)
        return if (dish != null) {
            ResponseEntity.ok(dish)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @PostMapping
    fun createDish(@Valid @RequestBody dto: DishCreateDto): ResponseEntity<DishDto> {
        val dish = dishService.createDish(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(dish)
    }
    
    @PutMapping("/{id}")
    fun updateDish(
        @PathVariable id: Long,
        @Valid @RequestBody dto: DishUpdateDto
    ): ResponseEntity<DishDto> {
        val dish = dishService.updateDish(id, dto)
        return if (dish != null) {
            ResponseEntity.ok(dish)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @DeleteMapping("/{id}")
    fun deleteDish(@PathVariable id: Long): ResponseEntity<Void> {
        val deleted = dishService.deleteDish(id)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

