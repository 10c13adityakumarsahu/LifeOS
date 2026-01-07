package com.example.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

enum class MessagePlatform {
    WHATSAPP, SMS
}

enum class MessageStatus {
    PENDING, SENT, FAILED
}

@Entity(tableName = "scheduled_messages")
data class ScheduledMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contactName: String,
    val contactNumber: String,
    val messageBody: String,
    val scheduledTime: LocalDateTime,
    val platform: MessagePlatform = MessagePlatform.WHATSAPP,
    val status: MessageStatus = MessageStatus.PENDING
)
