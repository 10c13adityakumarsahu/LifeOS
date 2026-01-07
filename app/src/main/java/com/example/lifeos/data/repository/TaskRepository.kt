package com.example.lifeos.data.repository

import com.example.lifeos.data.local.dao.TaskDao
import com.example.lifeos.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    
    val activeTasks: Flow<List<TaskEntity>> = taskDao.getActiveTasks() // Already sorted by Priority/EndTime

    fun getTasksInTimeRange(start: LocalDateTime, end: LocalDateTime): Flow<List<TaskEntity>> = 
        taskDao.getTasksInTimeRange(start, end)

    suspend fun addTask(task: TaskEntity) = taskDao.insertTask(task)
    
    suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)
    
    suspend fun deleteTask(task: TaskEntity) = taskDao.deleteTask(task)
    
    suspend fun getTaskById(id: Int): TaskEntity? = taskDao.getTaskById(id)
    
    suspend fun getAllActiveTasksSync(): List<TaskEntity> = taskDao.getActiveTasksSnapshot()
}
