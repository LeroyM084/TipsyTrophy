package com.supdevinci.tipsytrophy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.supdevinci.tipsytrophy.data.local.entities.DrinkLogs

@Dao
interface DrinkLogsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinkLog(drinkLog: DrinkLogs)

    @Query("SELECT * FROM DrinkLogs WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getLogsByUserId(userId: Long): List<DrinkLogs>
}
