package com.example.lifeos.data.repository

import com.example.lifeos.data.local.dao.SettingsDao
import com.example.lifeos.data.local.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class SettingsRepository(private val settingsDao: SettingsDao) {
    val settings: Flow<SettingsEntity?> = settingsDao.getSettings()

    suspend fun updateSettings(settings: SettingsEntity) {
        val existing = settingsDao.getSettings().firstOrNull()
        if (existing == null) {
            settingsDao.insertSettings(settings)
        } else {
            settingsDao.updateSettings(settings)
        }
    }
    
    suspend fun ensureSettingsExist() {
        if (settingsDao.getSettings().firstOrNull() == null) {
            settingsDao.insertSettings(SettingsEntity())
        }
    }

    suspend fun getSettingsSnapshot(): SettingsEntity? {
        return settingsDao.getSettingsSnapshot()
    }
}
