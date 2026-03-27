package com.supdevinci.tipsytrophy.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.tipsytrophy.data.SessionManager
import com.supdevinci.tipsytrophy.data.local.CocktailDatabase
import com.supdevinci.tipsytrophy.data.local.entities.Friends
import com.supdevinci.tipsytrophy.model.LeaderboardEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class LeaderBoardViewModel(application: Application) : AndroidViewModel(application) {

    private val currentUser = SessionManager.currentUser
    private val database = CocktailDatabase.getDatabase(application)

    var leaderboardData by mutableStateOf<List<LeaderboardEntry>>(emptyList())
        private set

    fun loadLeaderboard() {
        val currentUserId = currentUser?.id ?: return

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (System.currentTimeMillis() < timeInMillis) {
                add(Calendar.DAY_OF_YEAR, -7)
            }
        }
        val mondayTs = calendar.timeInMillis

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val board = database.friendsDao().getWeeklyLeaderboard(currentUserId, mondayTs)
                    .mapIndexed { index, entry -> entry.copy(position = index + 1) }
                withContext(Dispatchers.Main) {
                    leaderboardData = board
                }
            } catch (e: Exception) {
                Log.e("LeaderBoardVM", "Erreur leaderboard : ${e.message}")
            }
        }
    }

    suspend fun addFriend(friendUsername: String) : String {
        val currentUserId = currentUser?.id ?: return "ERROR"

        val friendUser = database.usersDao().getUserByName(friendUsername)
        if (friendUser == null) {
            Log.e("DatabaseError", "Utilisateur non trouvé : $friendUsername")
            return "ERROR"
        }

        val relation = Friends(id = 0, firstPersonId = currentUserId, secondPersonId = friendUser.id)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.friendsDao().insertNewFriend(relation)
                loadLeaderboard()
            } catch (e: Exception) {
                Log.e("DatabaseError", "Erreur lors de l'ajout de l'ami : ${e.message}")
            }
        }
        return "OK"
    }
}