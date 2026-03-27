package com.supdevinci.tipsytrophy.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.supdevinci.tipsytrophy.data.local.entities.Users

object SessionManager {
    var currentUser by mutableStateOf<Users?>(null)
    fun logout() {
        currentUser = null
    }
}