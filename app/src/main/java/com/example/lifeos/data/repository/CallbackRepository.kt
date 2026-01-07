package com.example.lifeos.data.repository

import com.example.lifeos.data.local.dao.CallbackLogDao
import com.example.lifeos.data.local.entity.CallbackLogEntity
import kotlinx.coroutines.flow.Flow

class CallbackRepository(private val dao: CallbackLogDao) {
    val allLogs: Flow<List<CallbackLogEntity>> = dao.getAllLogs()

    suspend fun logCallback(log: CallbackLogEntity) {
        dao.insertLog(log)
    }
}
