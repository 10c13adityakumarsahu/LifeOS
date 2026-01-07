package com.example.lifeos.data.repository

import com.example.lifeos.data.local.dao.EventDao
import com.example.lifeos.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {
    val allEvents: Flow<List<EventEntity>> = eventDao.getAllEvents()

    suspend fun addEvent(event: EventEntity) = eventDao.insertEvent(event)
    suspend fun updateEvent(event: EventEntity) = eventDao.updateEvent(event)
    suspend fun deleteEvent(event: EventEntity) = eventDao.deleteEvent(event)
}
