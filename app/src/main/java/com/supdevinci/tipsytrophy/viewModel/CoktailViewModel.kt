package com.supdevinci.tipsytrophy.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.tipsytrophy.data.RetrofitInstance
import com.supdevinci.tipsytrophy.data.SessionManager
import com.supdevinci.tipsytrophy.data.SessionManager.currentUser
import com.supdevinci.tipsytrophy.data.local.CocktailDatabase
import com.supdevinci.tipsytrophy.data.local.entities.DrinkLogs
import com.supdevinci.tipsytrophy.data.local.entities.Users
import com.supdevinci.tipsytrophy.model.Drink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class CoktailViewModel(application: Application) : AndroidViewModel(application) {

    var cocktailName = mutableStateOf("Recherchez un cocktail...")
    var foundCoktail by mutableStateOf<List<Drink>?>(null)
    var totalAlcoholLevel by mutableIntStateOf(-1)
    var isLoadingAlcohol by mutableStateOf(false)
    var userLogs by mutableStateOf<List<DrinkLogs>>(emptyList())
    var currentAlcoholLevel by mutableDoubleStateOf(0.0)

    private val database = CocktailDatabase.getDatabase(application)
    private val user: Users? = SessionManager.currentUser
    private val userId = user?.id ?: 0
    private val userWeight = user?.weight ?: 75
    private val userSex = user?.sex ?: "M"

    private var drinkVolume: Double = 0.0

  
    fun searchCoktailByName(text: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCoktailDetail(text)
                if (response.isSuccessful) {
                    val drinks = response.body()?.drinks
                    if (!drinks.isNullOrEmpty()) {
                        foundCoktail = drinks
                        cocktailName.value = ""
                    } else {
                        foundCoktail = null
                        cocktailName.value = "Aucun cocktail trouvé"
                        totalAlcoholLevel = -1
                    }
                }
            } catch (e: Exception) {
                cocktailName.value = "Erreur : ${e.message}"
            }
        }
    }

    suspend fun calculateGlobalAlcohol(drink: Drink): Int {
        isLoadingAlcohol = true
        val formattedIngredients = drink.getFormattedIngredients()

        var totalVolMl = 0.0
        var totalPureAlcMl = 0.0

        for ((name, measure) in formattedIngredients) {
            try {
                val response = RetrofitInstance.api.getIngredientDetail(name)
                if (response.isSuccessful) {
                    val ingredient = response.body()?.ingredients?.firstOrNull()
                    val abv = (ingredient?.abv ?: 0).toDouble()
                    val volumeMl = drink.parseMeasureToMl(measure)

                    totalVolMl += volumeMl
                    totalPureAlcMl += (volumeMl * abv / 100.0)
                }
            } catch (e: Exception) {
                totalVolMl += drink.parseMeasureToMl(measure)
            }
        }

        val finalAbv = if (totalVolMl > 0) {
            ((totalPureAlcMl / totalVolMl) * 100.0).toInt()
        } else {
            0
        }

        withContext(Dispatchers.Main) {
            totalAlcoholLevel = finalAbv
            drinkVolume = if (totalVolMl > 0) totalVolMl else 150.0
            isLoadingAlcohol = false
        }

        return finalAbv
    }

    fun addDrinkToLogs(drink: Drink) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val computedAbv = calculateGlobalAlcohol(drink)
                val currentUserId = currentUser?.id ?: 0

                val log = DrinkLogs(
                    userId = currentUserId,
                    label = drink.name,
                    abv = computedAbv,
                    size = drinkVolume,
                    createdAt = Date()
                )

                database.drinkLogsDao().insertDrinkLog(log)

                loadLogsByUserId()

            } catch (e: Exception) {
                Log.e("DatabaseError", "Erreur lors de l'ajout du log : ${e.message}")
            }
        }
    }


    fun loadLogsByUserId() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = database.drinkLogsDao().getLogsByUserId(userId)
                withContext(Dispatchers.Main) {
                    userLogs = result
                    calculateCurrentAlcoholLevel()
                }
            } catch (e: Exception) {
                Log.e("DB_ERROR", "Erreur récupération logs")
            }
        }
    }

    fun calculateCurrentAlcoholLevel() {
        val currentTime = System.currentTimeMillis()
        val twelveHoursInMs = 12 * 60 * 60 * 1000
        val startTime = currentTime - twelveHoursInMs

        val recentLogs = userLogs.filter { it.createdAt.time >= startTime }

        if (recentLogs.isEmpty()) {
            currentAlcoholLevel = 0.0
            return
        }

        var totalAlcoholGrams = 0.0
        recentLogs.forEach { log ->
            val grams = log.size * (log.abv.toDouble() / 100.0) * 0.8
            totalAlcoholGrams += grams
        }

        val weight = userWeight.toDouble()
        if (weight <= 0.0) return

        val k = if (userSex.contains("F", ignoreCase = true) || userSex.contains("Femme", ignoreCase = true)) 0.6 else 0.7

        val rawAlcoholLevel = totalAlcoholGrams / (weight * k)

        val firstLogTime = recentLogs.minByOrNull { it.createdAt.time }?.createdAt?.time ?: currentTime
        val hoursElapsed = (currentTime - firstLogTime).toDouble() / (1000 * 60 * 60)
        val elimination = hoursElapsed * 0.15

        val result = rawAlcoholLevel - elimination
        currentAlcoholLevel = if (result > 0) result else 0.0
    }
}