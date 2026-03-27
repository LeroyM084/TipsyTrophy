package com.supdevinci.tipsytrophy.model

import androidx.room.Ignore

data class LeaderboardEntry(
    val name: String,
    val drinkCount: Int,
    @Ignore val position : Int = 0
){
    constructor(name : String, drinkCount : Int) : this(name, drinkCount, 0)
}