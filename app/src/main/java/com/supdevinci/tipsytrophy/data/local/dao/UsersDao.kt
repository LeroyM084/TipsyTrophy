package com.supdevinci.tipsytrophy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.supdevinci.tipsytrophy.data.local.entities.Users

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Users) : Long

    @Query("SELECT * FROM Users WHERE id = :userId")
    suspend fun getUserById(userId : Long) : Users?

    @Update
    suspend fun updateUser(user: Users)

    @Query("""
        SELECT * FROM Users 
        WHERE id IN (
            SELECT firstPersonId FROM Friends WHERE secondPersonId = :userId
            UNION
            SELECT secondPersonId FROM Friends WHERE firstPersonId = :userId
        )
    """)
    suspend fun getUsersFriends(userId : Long) : List<Users>

    @Query("SELECT * FROM Users WHERE name = :username")
    suspend fun getUserByName(username: String) : Users?
}
