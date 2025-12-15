package com.maidtoorder.dishservice.config

import com.maidtoorder.dishservice.model.Dish
import com.maidtoorder.dishservice.repository.DishRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val dishRepository: DishRepository
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
    }
}
