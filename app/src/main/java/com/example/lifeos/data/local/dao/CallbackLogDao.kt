package com.example.lifeos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lifeos.data.local.entity.CallbackLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CallbackLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: CallbackLogEntity)

    @Query("SELECT * FROM callback_logs ORDER BY date DESC")
    fun getAllLogs(): Flow<List<CallbackLogEntity>>
}
