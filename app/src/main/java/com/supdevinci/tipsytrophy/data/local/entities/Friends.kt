package com.supdevinci.tipsytrophy.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Users::class,
            parentColumns = ["id"],
            childColumns = ["firstPersonId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Users::class,
            parentColumns = ["id"],
            childColumns = ["secondPersonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["firstPersonId"]),
        Index(value = ["secondPersonId"])
    ]
)
data class Friends(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstPersonId: Long,
    val secondPersonId: Long
)
