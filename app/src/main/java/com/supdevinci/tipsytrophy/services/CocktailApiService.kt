package com.supdevinci.tipsytrophy.services

import com.supdevinci.tipsytrophy.model.DrinkResponse
import com.supdevinci.tipsytrophy.model.IngredientResponse
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query

interface CocktailApiService {
        @GET("search.php")
        suspend fun getCoktailDetail(@Query("s") name: String): Response<DrinkResponse>

        @GET("search.php")
        suspend fun getIngredientDetail(@Query("i") name : String): Response<IngredientResponse>
}