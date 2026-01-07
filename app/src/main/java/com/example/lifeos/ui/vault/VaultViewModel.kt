package com.example.lifeos.ui.vault

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.local.entity.VaultItemEntity
import com.example.lifeos.data.local.entity.VaultItemType
import com.example.lifeos.data.repository.VaultRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VaultViewModel(
    private val vaultRepository: VaultRepository,
    private val settingsRepository: com.example.lifeos.data.repository.SettingsRepository
) : ViewModel() {

    val allItems: StateFlow<List<VaultItemEntity>> = vaultRepository.allItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addItem(title: String, content: String, type: VaultItemType) {
        viewModelScope.launch {
            val item = VaultItemEntity(
                title = title,
                content = content,
                type = type
            )
            vaultRepository.addItem(item)
        }
    }

    fun deleteItem(item: VaultItemEntity) {
        viewModelScope.launch {
            vaultRepository.deleteItem(item)
        }
    }

    fun updatePin(newPin: String) {
        viewModelScope.launch {
            val currentSettings = settingsRepository.settings.first() ?: com.example.lifeos.data.local.entity.SettingsEntity()
            settingsRepository.updateSettings(currentSettings.copy(pinCode = newPin))
        }
    }
}
