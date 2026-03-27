package com.supdevinci.tipsytrophy.model

import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName("strABV") val strABV: String?,
    @SerializedName("idIngredient") val id: String
) {
    val abv: Int
        get() = strABV?.toIntOrNull() ?: 0
}