package pkg.maid_to_order.network.mapper

import pkg.maid_to_order.data.model.Dish
import pkg.maid_to_order.network.dto.DishDto

object DishMapper {
    fun toDomain(dto: DishDto): Dish {
        return Dish(
            id = dto.id.toInt(),
            name = dto.name,
            description = dto.description,
            price = dto.price,
            category = dto.category,
            imageRes = null,
            imageUri = dto.imageUrl
        )
    }
    
    fun toDomainList(dtos: List<DishDto>): List<Dish> {
        return dtos.map { toDomain(it) }
    }
}

