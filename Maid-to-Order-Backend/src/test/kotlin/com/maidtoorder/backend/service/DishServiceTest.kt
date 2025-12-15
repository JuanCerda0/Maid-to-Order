package com.maidtoorder.backend.service

import com.maidtoorder.backend.dto.DishCreateDto
import com.maidtoorder.backend.dto.DishUpdateDto
import com.maidtoorder.backend.model.Dish
import com.maidtoorder.backend.repository.DishRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class DishServiceTest {
    
    private lateinit var dishRepository: DishRepository
    private lateinit var dishService: DishService
    
    @BeforeEach
    fun setUp() {
        dishRepository = mockk()
        dishService = DishService(dishRepository)
    }
    
    @Test
    fun `should get all dishes`() {
        // Given
        val dishes = listOf(
            Dish(id = 1, name = "Katsudon", description = "Test", price = 6700.0, category = "Platos Principales"),
            Dish(id = 2, name = "Ramen", description = "Test", price = 7400.0, category = "Sopas")
        )
        every { dishRepository.findAll() } returns dishes
        
        // When
        val result = dishService.getAllDishes()
        
        // Then
        assertEquals(2, result.size)
        assertEquals("Katsudon", result[0].name)
        verify { dishRepository.findAll() }
    }
    
    @Test
    fun `should get dish by id`() {
        // Given
        val dish = Dish(id = 1, name = "Katsudon", description = "Test", price = 6700.0, category = "Platos Principales")
        every { dishRepository.findById(1) } returns Optional.of(dish)
        
        // When
        val result = dishService.getDishById(1)
        
        // Then
        assertNotNull(result)
        assertEquals("Katsudon", result?.name)
        verify { dishRepository.findById(1) }
    }
    
    @Test
    fun `should return null when dish not found`() {
        // Given
        every { dishRepository.findById(999) } returns Optional.empty()
        
        // When
        val result = dishService.getDishById(999)
        
        // Then
        assertNull(result)
    }
    
    @Test
    fun `should create dish`() {
        // Given
        val createDto = DishCreateDto(
            name = "New Dish",
            description = "Test description",
            price = 5000.0,
            category = "Test"
        )
        val savedDish = Dish(id = 1, name = "New Dish", description = "Test description", price = 5000.0, category = "Test")
        every { dishRepository.save(any()) } returns savedDish
        
        // When
        val result = dishService.createDish(createDto)
        
        // Then
        assertEquals("New Dish", result.name)
        assertEquals(5000.0, result.price)
        verify { dishRepository.save(any()) }
    }
    
    @Test
    fun `should update dish`() {
        // Given
        val existingDish = Dish(id = 1, name = "Old Name", description = "Old Desc", price = 5000.0, category = "Old")
        val updateDto = DishUpdateDto(name = "New Name", price = 6000.0)
        val updatedDish = existingDish.copy(name = "New Name", price = 6000.0)
        
        every { dishRepository.findById(1) } returns Optional.of(existingDish)
        every { dishRepository.save(any()) } returns updatedDish
        
        // When
        val result = dishService.updateDish(1, updateDto)
        
        // Then
        assertNotNull(result)
        assertEquals("New Name", result?.name)
        assertEquals(6000.0, result?.price)
        verify { dishRepository.findById(1) }
        verify { dishRepository.save(any()) }
    }
    
    @Test
    fun `should delete dish`() {
        // Given
        every { dishRepository.existsById(1) } returns true
        every { dishRepository.deleteById(1) } just Runs
        
        // When
        val result = dishService.deleteDish(1)
        
        // Then
        assertTrue(result)
        verify { dishRepository.existsById(1) }
        verify { dishRepository.deleteById(1) }
    }
}

