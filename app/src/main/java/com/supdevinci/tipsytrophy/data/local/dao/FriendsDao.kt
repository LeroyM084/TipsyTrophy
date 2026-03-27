package com.supdevinci.tipsytrophy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.supdevinci.tipsytrophy.data.local.entities.Friends
import com.supdevinci.tipsytrophy.model.LeaderboardEntry
import com.supdevinci.tipsytrophy.data.local.entities.Users // Vérifie bien ce chemin

@Dao
interface FriendsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewFriend(friend : Friends)


    @Query("SELECT * FROM users WHERE id IN (:friendIds)")
    fun loadAllByIds(friendIds: List<Int>): List<Users>

    @Query("""
        SELECT 
            CASE 
                WHEN firstPersonId = :userId THEN secondPersonId 
                ELSE firstPersonId 
            END 
        FROM friends 
        WHERE firstPersonId = :userId OR secondPersonId = :userId
    """)
    fun getFriendIds(userId: Int): List<Int>

    // Merci Gemini pour la requête de fou
    @Query("""
    SELECT 
        U.name as name, 
        COUNT(D.id) as drinkCount
    FROM users U
    LEFT JOIN DrinkLogs D ON U.id = D.userId AND D.createdAt >= :mondayTimestamp
    WHERE U.id = :currentUserId 
       OR U.id IN (
           SELECT CASE WHEN firstPersonId = :currentUserId THEN secondPersonId ELSE firstPersonId END 
           FROM friends 
           WHERE firstPersonId = :currentUserId OR secondPersonId = :currentUserId
       )
    GROUP BY U.id
    ORDER BY drinkCount DESC
""")
    fun getWeeklyLeaderboard(currentUserId: Long, mondayTimestamp: Long): List<LeaderboardEntry>
}