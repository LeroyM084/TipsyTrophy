package com.supdevinci.tipsytrophy.viewModel

import android.app.Application
import android.util.Log
import com.supdevinci.tipsytrophy.data.RetrofitInstance
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.tipsytrophy.data.SessionManager
import com.supdevinci.tipsytrophy.data.SessionManager.currentUser
import com.supdevinci.tipsytrophy.data.local.entities.DrinkLogs
import com.supdevinci.tipsytrophy.model.Drink
import kotlinx.coroutines.launch
import java.util.Date
import com.supdevinci.tipsytrophy.data.local.dao.DrinkLogsDao
import kotlinx.coroutines.Dispatchers
import com.supdevinci.tipsytrophy.data.local.CocktailDatabase
import com.supdevinci.tipsytrophy.data.local.entities.Users
import kotlinx.coroutines.withContext

class CoktailViewModel(application : Application) : AndroidViewModel(application){
    var cocktailName = mutableStateOf("Recherchez un cocktail...")
    var foundCoktail by mutableStateOf<Drink?>(null)
    var totalAlcoholLevel by mutableIntStateOf(-1)
    var isLoadingAlcohol by mutableStateOf(false)
    var database = CocktailDatabase.getDatabase(application)
    var userLogs by mutableStateOf<List<DrinkLogs>>(emptyList())
    var user : Users? = SessionManager.currentUser
    var userId = user?.id ?: 0
    var userWeight = user?.weight ?: 75
    var userSex = user?.sex ?: "M"
    var currentAlcoholLevel by mutableStateOf(0.0)
    var drinkVolume : Double = 0.0



    fun searchCoktailByName(text: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCoktailDetail(text)
                if (response.isSuccessful) {
                    val drinkObj = response.body()?.drinks?.firstOrNull()
                    if (drinkObj != null) {
                        foundCoktail = drinkObj
                        cocktailName.value = drinkObj.name
                        calculateGlobalAlcohol(drinkObj)
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

    fun calculateGlobalAlcohol(drink: Drink) : Int {
        viewModelScope.launch {
            isLoadingAlcohol = true

            val formattedIngredients = drink.getFormattedIngredients()

            var totalVolumeMl = 0.0
            var totalPureAlcoholMl = 0.0

            for ((name, measure) in formattedIngredients) {
                try {
                    val response = RetrofitInstance.api.getIngredientDetail(name)
                    if (response.isSuccessful) {
                        val ingredient = response.body()?.ingredients?.firstOrNull()
                        val abv = (ingredient?.abv ?: 0).toDouble()

                        val volumeMl = drink.parseMeasureToMl(measure)

                        totalVolumeMl += volumeMl
                        totalPureAlcoholMl += (volumeMl * abv / 100.0)
                    }
                } catch (e: Exception) {
                    totalVolumeMl += drink.parseMeasureToMl(measure)
                }
            }

            totalAlcoholLevel = if (totalVolumeMl > 0) {
                ((totalPureAlcoholMl / totalVolumeMl) * 100.0).toInt()
            } else {
                0
            }
            drinkVolume = totalVolumeMl
        }
        return 0
    }



    fun addDrinkToLogs(drink: Drink) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentUserId = currentUser?.id ?: 0

                val log = DrinkLogs(
                    userId = currentUserId,
                    label = drink.name,
                    abv = totalAlcoholLevel,
                    size = drinkVolume,
                    createdAt = Date()
                )

                database.drinkLogsDao().insertDrinkLog(log)

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
                }
            } catch (e: Exception) {
                Log.e("DB_ERROR", "Erreur lors de la récupération des logs")
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
