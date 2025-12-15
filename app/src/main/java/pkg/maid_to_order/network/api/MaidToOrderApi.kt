package pkg.maid_to_order.network.api

import pkg.maid_to_order.network.dto.*
import retrofit2.Response
import retrofit2.http.*

interface MaidToOrderApi {
    
    @GET("dishes")
    suspend fun getDishes(
        @Query("category") category: String? = null,
        @Query("available") available: Boolean? = null,
        @Query("search") search: String? = null
    ): Response<List<DishDto>>
    
    @GET("dishes/{id}")
    suspend fun getDishById(@Path("id") id: Long): Response<DishDto>
    
    @POST("dishes")
    suspend fun createDish(@Body dish: DishCreateDto): Response<DishDto>
    
    @PUT("dishes/{id}")
    suspend fun updateDish(@Path("id") id: Long, @Body dish: DishCreateDto): Response<DishDto>
    
    @DELETE("dishes/{id}")
    suspend fun deleteDish(@Path("id") id: Long): Response<Unit>
    
    @GET("special-dishes")
    suspend fun getSpecialDishes(
        @Query("type") type: String? = null,
        @Query("today") today: Boolean = false
    ): Response<List<SpecialDishDto>>
    
    @GET("special-dishes/{id}")
    suspend fun getSpecialDishById(@Path("id") id: Long): Response<SpecialDishDto>
    
    @POST("special-dishes")
    suspend fun createSpecialDish(@Body dish: SpecialDishDto): Response<SpecialDishDto>
    
    @GET("orders")
    suspend fun getOrders(@Query("status") status: String? = null): Response<List<OrderDto>>
    
    @GET("orders/{id}")
    suspend fun getOrderById(@Path("id") id: Long): Response<OrderDto>
    
    @POST("orders")
    suspend fun createOrder(@Body order: OrderCreateDto): Response<OrderDto>
    
    @PUT("orders/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id") id: Long,
        @Body status: Map<String, String>
    ): Response<OrderDto>
}

