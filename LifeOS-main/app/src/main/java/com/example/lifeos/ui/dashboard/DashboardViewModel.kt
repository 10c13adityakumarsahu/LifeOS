package com.example.lifeos.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.local.entity.TaskEntity
import com.example.lifeos.data.repository.FinanceRepository
import com.example.lifeos.data.repository.FitnessRepository
import com.example.lifeos.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Duration

data class DashboardState(
    val doNow: List<TaskEntity> = emptyList(),
    val upNext: List<TaskEntity> = emptyList(),
    val laterToday: List<TaskEntity> = emptyList(),
    val totalExpensesToday: Double = 0.0,
    val waterIntakeToday: Int = 0,
    val upcomingEvents: List<com.example.lifeos.data.local.entity.EventEntity> = emptyList(),
    val upcomingBills: List<com.example.lifeos.data.local.entity.TransactionEntity> = emptyList()
)

class DashboardViewModel(
    private val taskRepository: TaskRepository,
    financeRepository: FinanceRepository,
    fitnessRepository: FitnessRepository,
    private val eventRepository: com.example.lifeos.data.repository.EventRepository,
    private val scheduledMessageRepository: com.example.lifeos.data.repository.ScheduledMessageRepository,
    private val alarmScheduler: com.example.lifeos.data.manager.AlarmScheduler
) : ViewModel() {

    private val tasks = taskRepository.activeTasks
    private val transactions = financeRepository.allTransactions
    private val fitnessLogs = fitnessRepository.getLogsForDay(LocalDateTime.now())
    private val events = eventRepository.allEvents

    init {
        refreshSmartSchedule()
    }

    private fun refreshSmartSchedule() {
        viewModelScope.launch {
            val activeTasks = taskRepository.getAllActiveTasksSync()
            val now = LocalDateTime.now()
            val currentTime = now.toLocalTime()
            
            activeTasks.forEach { task ->
                if (task.autoReschedule && !task.isCompleted && !task.isSkipped && task.startTime != null && task.endTime != null) {
                     // Check Threshold
                     if (task.thresholdTime != null && currentTime.isAfter(task.thresholdTime) && now.toLocalDate() == task.startTime.toLocalDate()) {
                         taskRepository.updateTask(task.copy(isSkipped = true))
                     } else if (now.isAfter(task.startTime.plusMinutes(task.flexibilityMinutes.toLong()))) {
                         // Reschedule to next 30 min block
                         val newStart = now.withSecond(0).withNano(0).plusMinutes(15)
                         val duration = java.time.Duration.between(task.startTime, task.endTime).toMinutes()
                         val newEnd = newStart.plusMinutes(duration)
                         
                         if (task.thresholdTime == null || newEnd.toLocalTime().isBefore(task.thresholdTime)) {
                              taskRepository.updateTask(task.copy(startTime = newStart, endTime = newEnd))
                              alarmScheduler.scheduleAlarm(newEnd, task.id, "Task Due: ${task.title}", "Your task is due now!")
                         }
                     }
                }
            }
        }
    }

    val state: StateFlow<DashboardState> = combine(tasks, transactions, fitnessLogs, events) { tasks, trans, logs, eventList ->
        val now = LocalDateTime.now()
        val endOfDay = LocalDate.now().atTime(23, 59)
        
        val sortedTasks = tasks
            .filter { (it.startTime ?: LocalDateTime.MAX) <= endOfDay && !it.isCompleted && !it.isSkipped }
            .sortedWith(compareBy<TaskEntity> { it.startTime ?: LocalDateTime.MAX }.thenByDescending { it.priority })
        
        // Categorization
        val doNow = sortedTasks.filter { 
             val st = it.startTime ?: return@filter false
             val et = it.endTime ?: return@filter false
             st <= now.plusMinutes(15) || (st <= now && et >= now)
        }.take(3)
        
        val upNext = sortedTasks.filter { 
            val st = it.startTime ?: return@filter false
            !doNow.contains(it) && st <= now.plusHours(6) 
        }.take(5)
            
        val laterToday = sortedTasks.filter { !doNow.contains(it) && !upNext.contains(it) }
        
        val upcomingBills = trans.filter { 
            it.isBill && !it.isPaid && it.dueDate != null && it.dueDate!! >= now && it.dueDate!! <= now.plusDays(3) 
        }.sortedBy { it.dueDate }

        DashboardState(
            doNow = doNow,
            upNext = upNext,
            laterToday = laterToday,
            waterIntakeToday = logs.filter { it.type == com.example.lifeos.data.local.entity.FitnessLogType.WATER }.sumOf { it.value }.toInt(),
            upcomingEvents = eventList.filter { it.date >= now && it.date <= now.plusDays(7) }.sortedBy { it.date },
            upcomingBills = upcomingBills
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardState())

    fun scheduleMessage(phone: String, message: String, minutesFromNow: Int, platform: com.example.lifeos.data.local.entity.MessagePlatform) {
        viewModelScope.launch {
            val scheduledTime = LocalDateTime.now().plusMinutes(minutesFromNow.toLong())
            val msgEntity = com.example.lifeos.data.local.entity.ScheduledMessageEntity(
                contactName = "Contact", // Placeholder, or ask user
                contactNumber = phone,
                messageBody = message,
                scheduledTime = scheduledTime,
                platform = platform
            )
            
            val id = scheduledMessageRepository.scheduleMessage(msgEntity)
            
            alarmScheduler.scheduleMessageAlarm(
                scheduledTime,
                id.toInt(),
                phone,
                message,
                platform.name
            )
        }
    }
}
