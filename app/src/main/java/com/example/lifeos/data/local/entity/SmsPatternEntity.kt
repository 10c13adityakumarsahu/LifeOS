package com.example.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lifeos.data.local.entity.TransactionType

@Entity(tableName = "sms_patterns")
data class SmsPatternEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pattern: String, // Regex string
    val senderId: String? = null, // Optional sender ID filter
    val type: TransactionType // Default type inferred from this pattern
)
