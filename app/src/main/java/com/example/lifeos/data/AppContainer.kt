package com.example.lifeos.data

import android.content.Context
import androidx.room.Room
import com.example.lifeos.data.local.LifeOSDatabase
import com.example.lifeos.data.repository.FinanceRepository
import com.example.lifeos.data.repository.FitnessRepository
import com.example.lifeos.data.repository.SettingsRepository
import com.example.lifeos.data.repository.TaskRepository

interface AppContainer {
    val taskRepository: TaskRepository
    val financeRepository: FinanceRepository
    val fitnessRepository: FitnessRepository
    val settingsRepository: SettingsRepository
    val eventRepository: com.example.lifeos.data.repository.EventRepository
    val scheduledMessageRepository: com.example.lifeos.data.repository.ScheduledMessageRepository
    val vaultRepository: com.example.lifeos.data.repository.VaultRepository
    val callbackRepository: com.example.lifeos.data.repository.CallbackRepository
    val alarmScheduler: com.example.lifeos.data.manager.AlarmScheduler
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val database: LifeOSDatabase by lazy {
        Room.databaseBuilder(
            context,
            LifeOSDatabase::class.java,
            "lifeos_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    override val taskRepository: TaskRepository by lazy {
        TaskRepository(database.taskDao())
    }

    override val financeRepository: FinanceRepository by lazy {
        FinanceRepository(database.financeDao())
    }

    override val fitnessRepository: FitnessRepository by lazy {
        FitnessRepository(database.fitnessDao())
    }
    
    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(database.settingsDao())
    }

    override val eventRepository: com.example.lifeos.data.repository.EventRepository by lazy {
        com.example.lifeos.data.repository.EventRepository(database.eventDao())
    }

    override val scheduledMessageRepository: com.example.lifeos.data.repository.ScheduledMessageRepository by lazy {
        com.example.lifeos.data.repository.ScheduledMessageRepository(database.scheduledMessageDao())
    }
    
    override val vaultRepository: com.example.lifeos.data.repository.VaultRepository by lazy {
        com.example.lifeos.data.repository.VaultRepository(database.vaultDao())
    }

    override val callbackRepository: com.example.lifeos.data.repository.CallbackRepository by lazy {
        com.example.lifeos.data.repository.CallbackRepository(database.callbackDao())
    }

    override val alarmScheduler: com.example.lifeos.data.manager.AlarmScheduler by lazy {
        com.example.lifeos.data.manager.AlarmScheduler(context)
    }
}
