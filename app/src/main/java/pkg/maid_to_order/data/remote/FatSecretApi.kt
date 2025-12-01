package pkg.maid_to_order.data.remote

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pkg.maid_to_order.data.remote.dto.TokenResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface FatSecretAuthApi {
    @POST("connect/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun getAccessToken(
        @Header("Authorization") authorization: String,
        @Body body: FormBody
    ): TokenResponse
}

interface FatSecretDataApi {
    @POST("rest/server.api")
    @FormUrlEncoded
    suspend fun searchFoods(
        @Header("Authorization") authorization: String,
        @Field("method") method: String = "foods.search",
        @Field("search_expression") searchExpression: String,
        @Field("format") format: String = "json"
    ): String
}

object FatSecretApi {
    private const val AUTH_URL = "https://oauth.fatsecret.com/"
    private const val API_URL = "https://platform.fatsecret.com/"

    // ⚠️ IMPORTANTE: En producción, estas credenciales deben estar en un backend
    private const val CLIENT_ID = "TU_CLIENT_ID"
    private const val CLIENT_SECRET = "TU_CLIENT_SECRET"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val authApi: FatSecretAuthApi = Retrofit.Builder()
        .baseUrl(AUTH_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FatSecretAuthApi::class.java)

    val dataApi: FatSecretDataApi = Retrofit.Builder()
        .baseUrl(API_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FatSecretDataApi::class.java)

    fun getCredentials() = Pair(CLIENT_ID, CLIENT_SECRET)
}