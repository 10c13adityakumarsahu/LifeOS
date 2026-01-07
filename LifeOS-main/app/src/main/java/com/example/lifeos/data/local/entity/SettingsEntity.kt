package com.example.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 0, // Singleton
    val isWaterReminderEnabled: Boolean = false,
    val waterReminderIntervalMinutes: Int = 60,
    val isSedentaryReminderEnabled: Boolean = false,
    val sedentaryReminderIntervalMinutes: Int = 45,
    val userName: String = "User",
    val areNotificationsEnabled: Boolean = true,
    val isSleepModeEnabled: Boolean = false, // Replaced by focus mode logic essentially, but keeping for compatibility if needed.
    val isAutoReplyEnabled: Boolean = true,
    val sleepWhitelist: String = "", // Comma containing numbers
    val pinCode: String? = null, // Null means no PIN set
    
    // Focus Mode additions
    val currentFocusMode: String = "None", // None, Sleep, Driving, Meeting
    val isSilentModeEnabledForFocus: Boolean = false,
    
    // Custom Replies
    val customReplySleep: String? = null,
    val customReplyDriving: String? = null,
    val customReplyMeeting: String? = null,
    
    // Recovery / System State
    val preFocusRingerMode: Int = -1, // -1 means none stored
    
    // Override Logic
    val allowOverride: Boolean = false
)
