package com.supdevinci.tipsytrophy.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.supdevinci.tipsytrophy.data.local.dao.UsersDao
import com.supdevinci.tipsytrophy.data.local.entities.DrinkLogs
import com.supdevinci.tipsytrophy.data.local.entities.Friends
import com.supdevinci.tipsytrophy.data.local.entities.Users
import com.supdevinci.tipsytrophy.data.local.dao.DrinkLogsDao
import com.supdevinci.tipsytrophy.data.local.dao.FriendsDao

@Database(entities = [Users::class, DrinkLogs::class, Friends::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CocktailDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun drinkLogsDao(): DrinkLogsDao
    abstract fun friendsDao(): FriendsDao



    companion object {
        @Volatile
        private var INSTANCE: CocktailDatabase? = null

        fun getDatabase(context: Context): CocktailDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CocktailDatabase::class.java,
                    "cocktail_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
