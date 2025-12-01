package pkg.maid_to_order.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FoodSearchResponse(
    val foods: FoodsWrapper?
)

data class FoodsWrapper(
    val food: List<FoodDto>?
)

data class FoodDto(
    @SerializedName("food_id")
    val foodId: String,
    @SerializedName("food_name")
    val foodName: String,
    @SerializedName("brand_name")
    val brandName: String?,
    @SerializedName("food_description")
    val foodDescription: String
)
