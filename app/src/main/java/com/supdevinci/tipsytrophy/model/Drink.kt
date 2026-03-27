package com.supdevinci.tipsytrophy.model

import com.google.gson.annotations.SerializedName

data class Drink(
    @SerializedName("idDrink") val id: String,
    @SerializedName("strDrink") val name: String,
    @SerializedName("strDrinkThumb") val imageUrl: String?,
    @SerializedName("strAlcoholic") val alcoholicType: String?,

    val strIngredient1: String?, val strIngredient2: String?, val strIngredient3: String?,
    val strIngredient4: String?, val strIngredient5: String?, val strIngredient6: String?,
    val strIngredient7: String?, val strIngredient8: String?, val strIngredient9: String?,
    val strIngredient10: String?, val strIngredient11: String?, val strIngredient12: String?,
    val strIngredient13: String?, val strIngredient14: String?, val strIngredient15: String?,

    val strMeasure1: String?, val strMeasure2: String?, val strMeasure3: String?,
    val strMeasure4: String?, val strMeasure5: String?, val strMeasure6: String?,
    val strMeasure7: String?, val strMeasure8: String?, val strMeasure9: String?,
    val strMeasure10: String?, val strMeasure11: String?, val strMeasure12: String?,
    val strMeasure13: String?, val strMeasure14: String?, val strMeasure15: String?
) {

    fun getFormattedIngredients(): List<Pair<String, String>> {
        val ingredients = mutableListOf<Pair<String, String>>()
        val rawIngredients = listOf(
            strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
            strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
            strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15
        )
        val rawMeasures = listOf(
            strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
            strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
            strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15
        )

        for (i in rawIngredients.indices) {
            val name = rawIngredients[i]
            if (!name.isNullOrBlank()) {
                val measure = rawMeasures.getOrNull(i) ?: ""
                ingredients.add(name.trim() to measure.trim())
            }
        }
        return ingredients
    }

   
    fun parseMeasureToMl(measure: String): Double {
        if (measure.isBlank()) return 30.0

        val cleanMeasure = measure.lowercase()
        
        val numberPattern = Regex("""(\d+/?\d*\.?\d*)""").find(cleanMeasure)?.value
        val amount = if (numberPattern != null) {
            if (numberPattern.contains("/")) {
                val parts = numberPattern.split("/")
                parts[0].toDouble() / parts[1].toDouble()
            } else {
                numberPattern.toDouble()
            }
        } else {
            1.0
        }

        return when {
            cleanMeasure.contains("oz") -> amount * 29.57
            cleanMeasure.contains("cl") -> amount * 10.0
            cleanMeasure.contains("ml") -> amount
            cleanMeasure.contains("tsp") || cleanMeasure.contains("c. à café") -> amount * 5.0
            cleanMeasure.contains("tbsp") || cleanMeasure.contains("c. à soupe") -> amount * 15.0
            cleanMeasure.contains("cup") || cleanMeasure.contains("tasse") -> amount * 240.0
            cleanMeasure.contains("shot") -> amount * 44.0
            cleanMeasure.contains("dash") || cleanMeasure.contains("trait") -> amount * 1.0
            cleanMeasure.contains("part") -> amount * 30.0 
            else -> amount * 30.0 
        }
    }
}
