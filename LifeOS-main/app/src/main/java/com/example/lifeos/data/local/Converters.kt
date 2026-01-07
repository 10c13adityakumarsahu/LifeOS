package com.example.lifeos.data.local

import androidx.room.TypeConverter
import com.example.lifeos.data.local.entity.Priority
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun fromPriority(value: String?): Priority? {
        return value?.let { Priority.valueOf(it) }
    }

    @TypeConverter
    fun priorityToString(priority: Priority?): String? {
        return priority?.name
    }

    @TypeConverter
    fun fromTransactionType(value: String?): com.example.lifeos.data.local.entity.TransactionType? {
        return value?.let { com.example.lifeos.data.local.entity.TransactionType.valueOf(it) }
    }

    @TypeConverter
    fun transactionTypeToString(type: com.example.lifeos.data.local.entity.TransactionType?): String? {
        return type?.name
    }

    @TypeConverter
    fun fromRecoveryStatus(value: String?): com.example.lifeos.data.local.entity.RecoveryStatus? {
        return value?.let { com.example.lifeos.data.local.entity.RecoveryStatus.valueOf(it) }
    }

    @TypeConverter
    fun recoveryStatusToString(status: com.example.lifeos.data.local.entity.RecoveryStatus?): String? {
        return status?.name
    }

    @TypeConverter
    fun fromFitnessLogType(value: String?): com.example.lifeos.data.local.entity.FitnessLogType? {
        return value?.let { com.example.lifeos.data.local.entity.FitnessLogType.valueOf(it) }
    }

    @TypeConverter
    fun fitnessLogTypeToString(type: com.example.lifeos.data.local.entity.FitnessLogType?): String? {
        return type?.name
    }
    @TypeConverter
    fun fromTaskCategory(value: String?): com.example.lifeos.data.local.entity.TaskCategory? {
        return value?.let { com.example.lifeos.data.local.entity.TaskCategory.valueOf(it) }
    }

    @TypeConverter
    fun taskCategoryToString(cat: com.example.lifeos.data.local.entity.TaskCategory?): String? {
        return cat?.name
    }

    @TypeConverter
    fun fromRecurrence(value: String?): com.example.lifeos.data.local.entity.Recurrence? {
        return value?.let { com.example.lifeos.data.local.entity.Recurrence.valueOf(it) }
    }

    @TypeConverter
    fun recurrenceToString(rec: com.example.lifeos.data.local.entity.Recurrence?): String? {
        return rec?.name
    }

    @TypeConverter
    fun fromLocalTime(value: String?): java.time.LocalTime? {
        return value?.let { java.time.LocalTime.parse(it) }
    }

    @TypeConverter
    fun localTimeToString(time: java.time.LocalTime?): String? {
        return time?.toString()
    }
}
