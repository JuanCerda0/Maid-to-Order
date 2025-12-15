package com.maidtoorder.backend.repository

import com.maidtoorder.backend.model.SpecialDish
import com.maidtoorder.backend.model.SpecialDishType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface SpecialDishRepository : JpaRepository<SpecialDish, Long> {
    fun findByType(type: SpecialDishType): List<SpecialDish>
    fun findByDate(date: LocalDate): List<SpecialDish>
    fun findByAvailableTrue(): List<SpecialDish>
    fun findByTypeAndAvailableTrue(type: SpecialDishType): List<SpecialDish>
    fun findByDateAndAvailableTrue(date: LocalDate): List<SpecialDish>
}

