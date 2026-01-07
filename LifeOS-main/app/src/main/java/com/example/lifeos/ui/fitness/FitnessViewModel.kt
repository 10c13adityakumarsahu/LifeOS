package com.example.lifeos.ui.fitness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.local.entity.FitnessLogEntity
import com.example.lifeos.data.local.entity.FitnessLogType
import com.example.lifeos.data.repository.FitnessRepository
import com.example.lifeos.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class FitnessViewModel(
    private val fitnessRepository: FitnessRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val todaysLogs = fitnessRepository.getLogsForDay(LocalDateTime.now())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun logWater(amount: Double) {
        viewModelScope.launch {
            fitnessRepository.logActivity(
                FitnessLogEntity(
                    type = FitnessLogType.WATER,
                    timestamp = LocalDateTime.now(),
                    value = amount
                )
            )
        }
    }
}
