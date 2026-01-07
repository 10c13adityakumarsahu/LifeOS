package com.example.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

enum class FitnessLogType {
    WATER, MOVEMENT
}

@Entity(tableName = "fitness_logs")
data class FitnessLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: FitnessLogType,
    val timestamp: LocalDateTime,
    val value: Double = 0.0 // e.g. ml of water or minutes of movement
)
