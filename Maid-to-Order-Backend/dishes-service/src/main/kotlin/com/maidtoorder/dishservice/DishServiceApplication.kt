package com.maidtoorder.dishservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DishServiceApplication

fun main(args: Array<String>) {
    runApplication<DishServiceApplication>(*args)
}
