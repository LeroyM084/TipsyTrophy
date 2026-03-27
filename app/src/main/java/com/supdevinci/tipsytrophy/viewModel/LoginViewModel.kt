package com.supdevinci.tipsytrophy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.tipsytrophy.data.SessionManager
import com.supdevinci.tipsytrophy.data.local.CocktailDatabase
import com.supdevinci.tipsytrophy.data.local.entities.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val database = CocktailDatabase.getDatabase(application)

    fun createUser(username: String, weight: Int, sex: String, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newUser = Users(
                    id = 0, 
                    name = username,
                    weight = weight,
                    sex = sex,
                    createdAt = Date(),
                    updatedAt = null,
                    deletedAt = null
                )
                database.usersDao().insertUser(newUser)
                
                val user = database.usersDao().getUserByName(username)
                withContext(Dispatchers.Main) {
                    SessionManager.currentUser = user
                    onComplete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateWeight(id: Long, weight: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.usersDao().getUserById(id.toLong())?.let { user ->
                    user.weight = weight
                    user.updatedAt = Date()
                    database.usersDao().updateUser(user)
                    
                    if (SessionManager.currentUser?.id == id) {
                        withContext(Dispatchers.Main) {
                            SessionManager.currentUser = user
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loginUser(username: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = database.usersDao().getUserByName(username)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        SessionManager.currentUser = user
                        onComplete(true)
                    } else {
                        onComplete(false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onComplete(false)
                }
            }
        }
    }
}
