package com.maidtoorder.specialservice.config

import com.maidtoorder.specialservice.model.SpecialDish
import com.maidtoorder.specialservice.model.SpecialDishType
import com.maidtoorder.specialservice.repository.SpecialDishRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DataInitializer(
    private val repository: SpecialDishRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (repository.count() == 0L) {
            repository.saveAll(
                listOf(
                    SpecialDish(
                        name = "Especialidad del Chef: Teriyaki Premium",
                        description = "Pollo teriyaki con salsa especial del chef, servido con arroz y vegetales",
                        price = 12000.0,
                        type = SpecialDishType.CHEF_SPECIAL,
                        date = LocalDate.now(),
                        available = true
                    ),
                    SpecialDish(
                        name = "Especial del Día: Ramen Tonkotsu",
                        description = "Ramen especial con caldo de cerdo de 12 horas de cocción",
                        price = 9500.0,
                        type = SpecialDishType.DAILY_SPECIAL,
                        date = LocalDate.now(),
                        available = true
                    )
                )
            )
        }
    }
}
