package pkg.maid_to_order.data.remote


import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.FormBody
import pkg.maid_to_order.data.model.FoodItem
import pkg.maid_to_order.data.remote.dto.FoodSearchResponse

class FatSecretService {
    private var accessToken: String? = null
    private var tokenExpiry: Long = 0
    private val tokenMutex = Mutex()
    private val gson = Gson()

    private suspend fun getValidToken(): String {
        tokenMutex.withLock {
            if (accessToken == null || System.currentTimeMillis() >= tokenExpiry) {
                val (clientId, clientSecret) = FatSecretApi.getCredentials()
                val credentials = "$clientId:$clientSecret"
                val encodedCredentials = Base64.encodeToString(
                    credentials.toByteArray(),
                    Base64.NO_WRAP
                )

                val formBody = FormBody.Builder()
                    .add("grant_type", "client_credentials")
                    .add("scope", "basic")
                    .build()

                val response = FatSecretApi.authApi.getAccessToken(
                    authorization = "Basic $encodedCredentials",
                    body = formBody
                )

                accessToken = response.accessToken
                tokenExpiry = System.currentTimeMillis() + (response.expiresIn * 1000)

                Log.d("FatSecret", "New token obtained, expires in ${response.expiresIn}s")
            }
            return accessToken!!
        }
    }

    suspend fun searchFoods(query: String): Result<List<FoodItem>> {
        return try {
            val token = getValidToken()
            val response = FatSecretApi.dataApi.searchFoods(
                authorization = "Bearer $token",
                searchExpression = query
            )

            val searchResponse = gson.fromJson(response, FoodSearchResponse::class.java)
            val foods = searchResponse.foods?.food?.map { dto ->
                parseFoodItem(dto.foodId, dto.foodName, dto.brandName, dto.foodDescription)
            } ?: emptyList()

            Result.success(foods)
        } catch (e: Exception) {
            Log.e("FatSecret", "Error searching foods", e)
            Result.failure(e)
        }
    }

    private fun parseFoodItem(
        id: String,
        name: String,
        brand: String?,
        description: String
    ): FoodItem {
        // Parsear descripción tipo: "Per 1 cup - Calories: 95kcal | Fat: 0.3g | Carbs: 25.1g | Protein: 0.5g"
        val parts = description.split("|")

        var calories = 0.0
        var protein = 0.0
        var carbs = 0.0
        var fat = 0.0
        var serving = "1 serving"

        parts.forEach { part ->
            val trimmed = part.trim()
            when {
                trimmed.startsWith("Per ") -> serving = trimmed.substringAfter("Per ").substringBefore("-").trim()
                "Calories:" in trimmed -> calories = trimmed.substringAfter("Calories:").replace("kcal", "").trim().toDoubleOrNull() ?: 0.0
                "Protein:" in trimmed -> protein = trimmed.substringAfter("Protein:").replace("g", "").trim().toDoubleOrNull() ?: 0.0
                "Carbs:" in trimmed -> carbs = trimmed.substringAfter("Carbs:").replace("g", "").trim().toDoubleOrNull() ?: 0.0
                "Fat:" in trimmed -> fat = trimmed.substringAfter("Fat:").replace("g", "").trim().toDoubleOrNull() ?: 0.0
            }
        }

        return FoodItem(
            id = id,
            name = name,
            brand = brand,
            description = description,
            calories = calories,
            protein = protein,
            carbs = carbs,
            fat = fat,
            servingSize = serving
        )
    }
}