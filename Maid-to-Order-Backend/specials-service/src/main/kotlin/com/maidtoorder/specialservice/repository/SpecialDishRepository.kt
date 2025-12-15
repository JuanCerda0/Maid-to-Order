package com.maidtoorder.specialservice.repository

import com.maidtoorder.specialservice.model.SpecialDish
import com.maidtoorder.specialservice.model.SpecialDishType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface SpecialDishRepository : JpaRepository<SpecialDish, Long> {
    fun findByType(type: SpecialDishType): List<SpecialDish>
    fun findByAvailableTrue(): List<SpecialDish>
    fun findByTypeAndAvailableTrue(type: SpecialDishType): List<SpecialDish>
    fun findByDateAndAvailableTrue(date: LocalDate): List<SpecialDish>
}
