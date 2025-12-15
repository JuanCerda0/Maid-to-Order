package com.maidtoorder.backend.config

import com.maidtoorder.backend.model.Dish
import com.maidtoorder.backend.model.SpecialDish
import com.maidtoorder.backend.model.SpecialDishType
import com.maidtoorder.backend.repository.DishRepository
import com.maidtoorder.backend.repository.SpecialDishRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DataInitializer(
    private val dishRepository: DishRepository,
    private val specialDishRepository: SpecialDishRepository
) : CommandLineRunner {
    
    override fun run(vararg args: String?) {
        if (dishRepository.count() == 0L) {
            dishRepository.saveAll(
                listOf(
                    Dish(name = "Katsudon", description = "Cerdo frito con arroz y huevo", price = 6700.0, category = "Platos Principales", available = true),
                    Dish(name = "Ramen", description = "Sopa japonesa con fideos y cerdo", price = 7400.0, category = "Sopas", available = true),
                    Dish(name = "Onigiri", description = "Bola de arroz rellena con salmón", price = 4000.0, category = "Aperitivos", available = true),
                    Dish(name = "Curry Japonés", description = "Arroz con curry suave y carne", price = 8500.0, category = "Platos Principales", available = true),
                    Dish(name = "Sushi Roll", description = "Roll de sushi con salmón y aguacate", price = 9200.0, category = "Aperitivos", available = true),
                    Dish(name = "Tempura", description = "Verduras y mariscos rebozados", price = 7800.0, category = "Aperitivos", available = true)
                )
            )
        }
        
        if (specialDishRepository.count() == 0L) {
            specialDishRepository.saveAll(
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

