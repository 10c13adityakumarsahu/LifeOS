package com.example.lifeos.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lifeos.LifeOSApplication
import com.example.lifeos.ui.dashboard.DashboardViewModel
import com.example.lifeos.ui.finance.FinanceViewModel
import com.example.lifeos.ui.fitness.FitnessViewModel
import com.example.lifeos.ui.settings.SettingsViewModel
import com.example.lifeos.ui.tasks.TasksViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            DashboardViewModel(
                lifeOSApplication().container.taskRepository,
                lifeOSApplication().container.financeRepository,
                lifeOSApplication().container.fitnessRepository,
                lifeOSApplication().container.eventRepository,
                lifeOSApplication().container.scheduledMessageRepository,
                lifeOSApplication().container.alarmScheduler
            )
        }
        initializer {
            TasksViewModel(
                lifeOSApplication().container.taskRepository,
                lifeOSApplication().container.alarmScheduler
            )
        }
        initializer {
            FinanceViewModel(lifeOSApplication().container.financeRepository)
        }
        initializer {
            FitnessViewModel(
                lifeOSApplication().container.fitnessRepository,
                lifeOSApplication().container.settingsRepository
            )
        }
        initializer {
            SettingsViewModel(
                lifeOSApplication().container.settingsRepository,
                lifeOSApplication().container.callbackRepository
            )
        }
        initializer {
            // Events ViewModel with dependencies
            com.example.lifeos.ui.events.EventsViewModel(
                lifeOSApplication().container.eventRepository,
                lifeOSApplication().container.scheduledMessageRepository,
                lifeOSApplication().container.alarmScheduler
            )
        }
        initializer {
            com.example.lifeos.ui.vault.VaultViewModel(
                lifeOSApplication().container.vaultRepository,
                lifeOSApplication().container.settingsRepository
            )
        }
    }
}

fun CreationExtras.lifeOSApplication(): LifeOSApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LifeOSApplication)
