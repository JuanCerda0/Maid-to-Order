package com.maidtoorder.dishservice.service

import com.maidtoorder.dishservice.dto.DishCreateDto
import com.maidtoorder.dishservice.dto.DishUpdateDto
import com.maidtoorder.dishservice.model.Dish
import com.maidtoorder.dishservice.repository.DishRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

class DishServiceTest {

    private lateinit var dishRepository: DishRepository
    private lateinit var dishService: DishService

    @BeforeEach
    fun setup() {
        dishRepository = mockk()
        dishService = DishService(dishRepository)
    }

    @Test
    fun `should return all dishes`() {
        val dishes = listOf(
            Dish(id = 1, name = "Katsudon", description = "Test", price = 6700.0, category = "Platos"),
            Dish(id = 2, name = "Ramen", description = "Test", price = 7400.0, category = "Sopas")
        )
        every { dishRepository.findAll() } returns dishes

        val result = dishService.getAllDishes()

        assertEquals(2, result.size)
        assertEquals("Katsudon", result.first().name)
        verify { dishRepository.findAll() }
    }

    @Test
    fun `should create dish`() {
        val createDto = DishCreateDto("Nuevo", "Desc", 5000.0)
        val savedDish = Dish(id = 5, name = "Nuevo", description = "Desc", price = 5000.0, category = "General")
        every { dishRepository.save(any()) } returns savedDish

        val result = dishService.createDish(createDto)

        assertEquals(5, result.id)
        assertEquals("Nuevo", result.name)
        verify { dishRepository.save(any()) }
    }

    @Test
    fun `should update dish`() {
        val existing = Dish(id = 3, name = "Viejo", description = "desc", price = 4000.0, category = "Old")
        val update = DishUpdateDto(name = "Nuevo", price = 6000.0)

        every { dishRepository.findById(3) } returns Optional.of(existing)
        every { dishRepository.save(any()) } answers { firstArg() }

        val result = dishService.updateDish(3, update)

        assertEquals("Nuevo", result?.name)
        assertEquals(6000.0, result?.price)
        verify { dishRepository.save(any()) }
    }

    @Test
    fun `should delete dish`() {
        every { dishRepository.existsById(9) } returns true
        every { dishRepository.deleteById(9) } just Runs

        val success = dishService.deleteDish(9)

        assertTrue(success)
        verify { dishRepository.deleteById(9) }
    }
}
