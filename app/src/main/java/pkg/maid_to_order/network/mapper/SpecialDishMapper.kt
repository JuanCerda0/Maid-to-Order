package pkg.maid_to_order.network.mapper

import pkg.maid_to_order.data.model.Dish
import pkg.maid_to_order.network.dto.SpecialDishDto

object SpecialDishMapper {
    fun toDomain(dto: SpecialDishDto): Dish {
        return Dish(
            id = dto.id.toInt(),
            name = dto.name,
            description = dto.description,
            price = dto.price,
            category = when (dto.type) {
                "CHEF_SPECIAL" -> "Especialidad del Chef"
                "DAILY_SPECIAL" -> "Especial del Día"
                "SEASONAL" -> "Especialidad de Temporada"
                else -> "Especialidades"
            },
            imageRes = null,
            imageUri = dto.imageUrl
        )
    }
    
    fun toDomainList(dtos: List<SpecialDishDto>): List<Dish> {
        return dtos.map { toDomain(it) }
    }
}

