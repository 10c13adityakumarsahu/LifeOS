package com.example.lifeos.data.repository

import com.example.lifeos.data.local.dao.FitnessDao
import com.example.lifeos.data.local.entity.FitnessLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class FitnessRepository(private val fitnessDao: FitnessDao) {
    val allLogs: Flow<List<FitnessLogEntity>> = fitnessDao.getAllLogs()

    suspend fun logActivity(log: FitnessLogEntity) = fitnessDao.insertLog(log)
    
    fun getLogsForDay(date: LocalDateTime): Flow<List<FitnessLogEntity>> {
        val startOfDay = date.toLocalDate().atStartOfDay()
        val endOfDay = date.toLocalDate().plusDays(1).atStartOfDay()
        return fitnessDao.getLogsInTimeRange(startOfDay, endOfDay)
    }
    suspend fun getLogsForDaySnapshot(date: LocalDateTime): List<FitnessLogEntity> {
        val startOfDay = date.toLocalDate().atStartOfDay()
        val endOfDay = date.toLocalDate().plusDays(1).atStartOfDay()
        return fitnessDao.getLogsInTimeRangeSnapshot(startOfDay, endOfDay)
    }
}
