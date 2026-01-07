package com.example.lifeos.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.repository.CallbackRepository
import com.example.lifeos.data.local.entity.SettingsEntity
import com.example.lifeos.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val callbackRepository: CallbackRepository
) : ViewModel() {
    val settings = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val callbackLogs = callbackRepository.allLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            settingsRepository.ensureSettingsExist()
        }
    }

    fun updateWaterReminder(enabled: Boolean) {
        viewModelScope.launch {
            settings.value?.let { current ->
                settingsRepository.updateSettings(current.copy(isWaterReminderEnabled = enabled))
            }
        }
    }

    fun updateSedentaryReminder(enabled: Boolean) {
        viewModelScope.launch {
            settings.value?.let { current ->
                settingsRepository.updateSettings(current.copy(isSedentaryReminderEnabled = enabled))
            }
        }
    }
    
    fun updateWaterInterval(minutes: Int) {
        viewModelScope.launch {
            settings.value?.let { current ->
                settingsRepository.updateSettings(current.copy(waterReminderIntervalMinutes = minutes))
            }
        }
    }

    fun updateSedentaryInterval(minutes: Int) {
        viewModelScope.launch {
            settings.value?.let { current ->
                settingsRepository.updateSettings(current.copy(sedentaryReminderIntervalMinutes = minutes))
            }
        }
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settings.value?.let { current ->
                settingsRepository.updateSettings(current.copy(areNotificationsEnabled = enabled))
            }
        }
    }

    fun updateSleepMode(enabled: Boolean) {
        // Legacy support mapping to new system
        updateFocusMode(if (enabled) "Sleep" else "None")
    }

    fun updateFocusMode(mode: String) {
        viewModelScope.launch {
            settings.value?.let { current ->
                val isFocusActive = mode != "None"
                
                // Reset call tracking for every mode change
                com.example.lifeos.data.manager.FocusManager.resetCounts()
                
                val updated = current.copy(
                    currentFocusMode = mode,
                    isSleepModeEnabled = (mode == "Sleep"), // Sync legacy
                    isWaterReminderEnabled = !isFocusActive, // Disable reminders in focus mode
                    isSedentaryReminderEnabled = !isFocusActive
                )
                settingsRepository.updateSettings(updated)
            }
        }
    }

    fun updateSilentModeForFocus(enabled: Boolean) {
        viewModelScope.launch {
            settings.value?.let { current ->
                settingsRepository.updateSettings(current.copy(isSilentModeEnabledForFocus = enabled))
            }
        }
    }

    fun addSleepContact(number: String) {
        viewModelScope.launch {
            settings.value?.let { current ->
                val list = current.sleepWhitelist.split(",").filter { it.isNotBlank() }.toMutableList()
                if (!list.contains(number)) {
                    list.add(number)
                    settingsRepository.updateSettings(current.copy(sleepWhitelist = list.joinToString(",")))
                }
            }
        }
    }

    fun removeSleepContact(number: String) {
        viewModelScope.launch {
            settings.value?.let { current ->
                val list = current.sleepWhitelist.split(",").filter { it.isNotBlank() }.toMutableList()
                if (list.remove(number)) {
                    settingsRepository.updateSettings(current.copy(sleepWhitelist = list.joinToString(",")))
                }
            }
        }
    }

    fun updateAutoReply(enabled: Boolean) {
        viewModelScope.launch {
            settings.value?.let { current ->
                settingsRepository.updateSettings(current.copy(isAutoReplyEnabled = enabled))
            }
        }
    }

    fun updateUserName(name: String) {
        viewModelScope.launch {
            settings.value?.let { current ->
                settingsRepository.updateSettings(current.copy(userName = name))
            }
        }
    }

    fun setPin(pin: String) {
        viewModelScope.launch {
            settings.value?.let { current ->
                settingsRepository.updateSettings(current.copy(pinCode = pin))
            }
        }
    }

    fun removePin() {
        viewModelScope.launch {
            settings.value?.let { current ->
                settingsRepository.updateSettings(current.copy(pinCode = null))
            }
        }
    }
    fun updateFocusModeReply(mode: String, reply: String) {
        viewModelScope.launch {
            settings.value?.let { current ->
                val updated = when(mode) {
                    "Sleep" -> current.copy(customReplySleep = reply)
                    "Driving" -> current.copy(customReplyDriving = reply)
                    "Meeting" -> current.copy(customReplyMeeting = reply)
                    else -> current
                }
                settingsRepository.updateSettings(updated)
            }
        }
    }

    fun updatePreFocusRingerMode(mode: Int) {
        viewModelScope.launch {
            settings.value?.let { current ->
                settingsRepository.updateSettings(current.copy(preFocusRingerMode = mode))
            }
        }
    }
    
    fun updateAllowOverride(allowed: Boolean) {
        viewModelScope.launch {
            settings.value?.let { current ->
                settingsRepository.updateSettings(current.copy(allowOverride = allowed))
            }
        }
    }
}
