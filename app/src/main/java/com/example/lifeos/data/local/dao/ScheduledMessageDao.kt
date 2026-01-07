package com.example.lifeos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lifeos.data.local.entity.ScheduledMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduledMessageDao {
    @Query("SELECT * FROM scheduled_messages ORDER BY scheduledTime ASC")
    fun getAllMessages(): Flow<List<ScheduledMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ScheduledMessageEntity): Long

    @Update
    suspend fun updateMessage(message: ScheduledMessageEntity)

    @Delete
    suspend fun deleteMessage(message: ScheduledMessageEntity)
    
    @Query("SELECT * FROM scheduled_messages WHERE id = :id")
    suspend fun getMessageById(id: Int): ScheduledMessageEntity?
}
