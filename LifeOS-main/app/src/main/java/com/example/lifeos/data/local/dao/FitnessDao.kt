package com.example.lifeos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lifeos.data.local.entity.FitnessLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface FitnessDao {
    @Query("SELECT * FROM fitness_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<FitnessLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: FitnessLogEntity): Long
    
    @Query("SELECT * FROM fitness_logs WHERE timestamp >= :start AND timestamp <= :end")
    fun getLogsInTimeRange(start: LocalDateTime, end: LocalDateTime): Flow<List<FitnessLogEntity>>

    @Query("SELECT * FROM fitness_logs WHERE timestamp >= :start AND timestamp <= :end")
    suspend fun getLogsInTimeRangeSnapshot(start: LocalDateTime, end: LocalDateTime): List<FitnessLogEntity>
}
