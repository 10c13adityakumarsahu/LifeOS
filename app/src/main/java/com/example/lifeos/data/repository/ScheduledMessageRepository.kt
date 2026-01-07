package com.example.lifeos.data.repository

import com.example.lifeos.data.local.dao.ScheduledMessageDao
import com.example.lifeos.data.local.entity.ScheduledMessageEntity

class ScheduledMessageRepository(private val dao: ScheduledMessageDao) {
    val allMessages = dao.getAllMessages()

    suspend fun scheduleMessage(message: ScheduledMessageEntity): Long {
        return dao.insertMessage(message)
    }

    suspend fun updateMessage(message: ScheduledMessageEntity) {
        dao.updateMessage(message)
    }

    suspend fun deleteMessage(message: ScheduledMessageEntity) {
        dao.deleteMessage(message)
    }
    
    suspend fun getMessageById(id: Int): ScheduledMessageEntity? {
        return dao.getMessageById(id)
    }
}
