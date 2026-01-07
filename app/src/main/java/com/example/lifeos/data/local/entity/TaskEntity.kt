package com.example.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.LocalTime

enum class Priority {
    LOW, MEDIUM, HIGH, CRITICAL
}

enum class TaskCategory {
    WORK, HABIT, CHORE, PERSONAL, HEALTH, OTHER
}

enum class Recurrence {
    NONE, DAILY, WEEKLY, MONTHLY
}

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null, // Deadline
    val priority: Priority,
    val category: TaskCategory = TaskCategory.OTHER,
    val flexibilityMinutes: Int = 0, 
    val estimatedDurationMinutes: Int = 30,
    
    // Status
    val isCompleted: Boolean = false,
    val isSkipped: Boolean = false,
    
    // Recurrence
    val recurrence: Recurrence = Recurrence.NONE,
    
    // Smart Scheduling
    val autoReschedule: Boolean = false,
    val thresholdTime: LocalTime? = null,
    
    // New vision fields
    val isGoal: Boolean = false,
    val hasSpawnedRecurrence: Boolean = false,
    val spawnedTaskId: Int? = null
)
