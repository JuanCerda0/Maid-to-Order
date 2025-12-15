package com.maidtoorder.dishservice.repository

import com.maidtoorder.dishservice.model.Dish
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DishRepository : JpaRepository<Dish, Long> {
    fun findByCategory(category: String): List<Dish>
    fun findByAvailableTrue(): List<Dish>
    fun findByNameContainingIgnoreCase(name: String): List<Dish>
    fun findByCategoryAndAvailableTrue(category: String): List<Dish>
}
