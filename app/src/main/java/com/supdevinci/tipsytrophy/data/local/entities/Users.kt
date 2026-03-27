package com.supdevinci.tipsytrophy.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity
data class Users(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    var weight : Int,
    val sex : String,

    // timestamp
    val createdAt : Date,
    var updatedAt : Date?,
    var deletedAt : Date?
)
