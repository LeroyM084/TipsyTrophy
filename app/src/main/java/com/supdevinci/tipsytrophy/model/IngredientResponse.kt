package com.supdevinci.tipsytrophy.model

import com.google.gson.annotations.SerializedName

data class IngredientResponse(
    @SerializedName("ingredients")
    val ingredients: List<Ingredient>?
)