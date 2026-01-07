package com.example.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "callback_logs")
data class CallbackLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: LocalDateTime,
    val phoneNumber: String,
    val messageSent: String
)
