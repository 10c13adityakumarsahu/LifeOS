package com.example.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

enum class EventType {
    BIRTHDAY, ANNIVERSARY, OTHER
}

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val date: LocalDateTime,
    val type: EventType,
    val reminderMinutes: Int = 1440, // Default 1 day before
    
    // New Features
    val isRecursive: Boolean = false,
    val scheduledMessageBody: String? = null,
    val scheduledMessageContact: String? = null,
    val customCategory: String? = null,
    val description: String? = null,
    val scheduledMessageId: Long? = null,
    val scheduledMessagePlatform: MessagePlatform? = null
)
