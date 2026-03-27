package com.supdevinci.tipsytrophy.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Users::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class DrinkLogs(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Long,
    val label: String,
    val abv: Int,
    val size : Double,

    // timestamps
    val createdAt: Date,
    var updatedAt: Date? = null,
    var deletedAt: Date? = null
)