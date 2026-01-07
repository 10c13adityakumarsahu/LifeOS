package com.example.lifeos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lifeos.data.local.dao.FinanceDao
import com.example.lifeos.data.local.dao.FitnessDao
import com.example.lifeos.data.local.dao.SettingsDao
import com.example.lifeos.data.local.dao.TaskDao
import com.example.lifeos.data.local.entity.FitnessLogEntity
import com.example.lifeos.data.local.entity.SettingsEntity
import com.example.lifeos.data.local.entity.TaskEntity
import com.example.lifeos.data.local.entity.TransactionEntity

@Database(
    entities = [
        TaskEntity::class, 
        TransactionEntity::class, 
        FitnessLogEntity::class, 
        SettingsEntity::class,
        com.example.lifeos.data.local.entity.GoalEntity::class,
        com.example.lifeos.data.local.entity.EventEntity::class,
        com.example.lifeos.data.local.entity.ScheduledMessageEntity::class,
        com.example.lifeos.data.local.entity.VaultItemEntity::class,
        com.example.lifeos.data.local.entity.CallbackLogEntity::class,
        com.example.lifeos.data.local.entity.SmsPatternEntity::class,
        com.example.lifeos.data.local.entity.UntrackedTransactionEntity::class,
        com.example.lifeos.data.local.entity.PersonEntity::class
    ], 
    version = 14,
    exportSchema = false
)
@TypeConverters(Converters::class, LocalDateConverter::class)
abstract class LifeOSDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun financeDao(): FinanceDao
    abstract fun fitnessDao(): FitnessDao
    abstract fun settingsDao(): SettingsDao
    abstract fun eventDao(): com.example.lifeos.data.local.dao.EventDao
    abstract fun scheduledMessageDao(): com.example.lifeos.data.local.dao.ScheduledMessageDao
    abstract fun vaultDao(): com.example.lifeos.data.local.dao.VaultDao
    abstract fun callbackDao(): com.example.lifeos.data.local.dao.CallbackLogDao
}
