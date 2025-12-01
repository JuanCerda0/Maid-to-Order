package pkg.maid_to_order.repository

import pkg.maid_to_order.data.model.FoodItem
import pkg.maid_to_order.data.remote.FatSecretService

class NutritionRepository {
    private val fatSecretService = FatSecretService()

    suspend fun searchFoods(query: String): Result<List<FoodItem>> {
        return fatSecretService.searchFoods(query)
    }
}