package com.supdevinci.tipsytrophy.model

import com.google.gson.annotations.SerializedName

data class DrinkResponse(
    @SerializedName("drinks")
    val drinks: List<Drink>?
)