package com.example.lifeos.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.local.entity.Priority
import com.example.lifeos.data.local.entity.TaskEntity
import com.example.lifeos.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Duration

class TasksViewModel(
    private val taskRepository: TaskRepository,
    private val alarmScheduler: com.example.lifeos.data.manager.AlarmScheduler
) : ViewModel() {
    val allTasks: StateFlow<List<TaskEntity>> = taskRepository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTask(
        title: String, 
        description: String, 
        start: LocalDateTime? = null, 
        end: LocalDateTime? = null, 
        priority: Priority, 
        category: com.example.lifeos.data.local.entity.TaskCategory = com.example.lifeos.data.local.entity.TaskCategory.OTHER,
        recurrence: com.example.lifeos.data.local.entity.Recurrence = com.example.lifeos.data.local.entity.Recurrence.NONE,
        flexibility: Int = 0, 
        duration: Int = 30,
        autoReschedule: Boolean = false,
        thresholdTime: java.time.LocalTime? = null,
        isGoal: Boolean = false
    ) {
        viewModelScope.launch {
            val task = TaskEntity(
                title = title,
                description = description,
                startTime = start,
                endTime = end,
                priority = priority,
                category = category,
                recurrence = recurrence,
                flexibilityMinutes = flexibility,
                estimatedDurationMinutes = duration,
                autoReschedule = autoReschedule,
                thresholdTime = thresholdTime,
                isGoal = isGoal
            )
            
            // 1. Add the task
            val newId = taskRepository.addTask(task)
            var createdTask = task.copy(id = newId.toInt())
            
            if (end != null) {
                scheduleAlarms(createdTask)
            }
            
            // 2. Trigger smart rearrangement to resolve overlaps
            // We only trigger this if the new task has times set
            if (start != null && end != null) {
                 rescheduleOverlaps()
            }
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
            scheduleAlarms(task)
            // Trigger rearrangement if times changed
            if (task.startTime != null && task.endTime != null) {
                rescheduleOverlaps()
            }
        }
    }

    fun toggleTaskCompletion(task: TaskEntity, isChecked: Boolean) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = isChecked)
            
            if (isChecked) {
                // Cancel alarms for completed task
                alarmScheduler.cancelAlarm(task.id)
                alarmScheduler.cancelAlarm(task.id + 100000)
                
                // Handle Recurrence
                if (task.recurrence != com.example.lifeos.data.local.entity.Recurrence.NONE && !task.hasSpawnedRecurrence) {
                    val nextStart = task.startTime?.let {
                        when(task.recurrence) {
                            com.example.lifeos.data.local.entity.Recurrence.DAILY -> it.plusDays(1)
                            com.example.lifeos.data.local.entity.Recurrence.WEEKLY -> it.plusWeeks(1)
                            com.example.lifeos.data.local.entity.Recurrence.MONTHLY -> it.plusMonths(1)
                            else -> it
                        }
                    }
                    val nextEnd = task.endTime?.let {
                        when(task.recurrence) {
                            com.example.lifeos.data.local.entity.Recurrence.DAILY -> it.plusDays(1)
                            com.example.lifeos.data.local.entity.Recurrence.WEEKLY -> it.plusWeeks(1)
                            com.example.lifeos.data.local.entity.Recurrence.MONTHLY -> it.plusMonths(1)
                            else -> it
                        }
                    }
                    
                    val newTask = task.copy(
                        id = 0, 
                        startTime = nextStart,
                        endTime = nextEnd,
                        isCompleted = false,
                        isSkipped = false,
                        hasSpawnedRecurrence = false,
                        spawnedTaskId = null
                    )
                    val newId = taskRepository.addTask(newTask)
                    if (nextEnd != null) {
                        scheduleAlarms(newTask.copy(id = newId.toInt()))
                    }
                    
                    taskRepository.updateTask(updatedTask.copy(hasSpawnedRecurrence = true, spawnedTaskId = newId.toInt()))
                } else {
                    taskRepository.updateTask(updatedTask)
                }
            } else {
                 if (task.hasSpawnedRecurrence && task.spawnedTaskId != null) {
                     val spawnedTask = allTasks.value.find { it.id == task.spawnedTaskId }
                     if (spawnedTask != null && !spawnedTask.isCompleted) {
                         taskRepository.deleteTask(spawnedTask)
                         alarmScheduler.cancelAlarm(spawnedTask.id)
                         alarmScheduler.cancelAlarm(spawnedTask.id + 100000)
                     }
                 }
                 taskRepository.updateTask(updatedTask.copy(hasSpawnedRecurrence = false, spawnedTaskId = null))
                 if (task.endTime != null) {
                    scheduleAlarms(updatedTask)
                 }
            }
        }
    }
    
    fun refreshSmartSchedule() {
        viewModelScope.launch {
            val activeTasks = taskRepository.getAllActiveTasksSync()
            val now = LocalDateTime.now()
            var hasChanges = false
            
            // 1. Identify Overdue/Missed Tasks and Move them to "Now" bucket
            activeTasks.filter { it.autoReschedule && !it.isCompleted && !it.isSkipped && it.startTime != null && it.endTime != null }
                .sortedBy { it.startTime }
                .forEach { task ->
                    val start = task.startTime!!
                    val end = task.endTime!!
                    val flexibleStart = start.plusMinutes(task.flexibilityMinutes.toLong())
                    
                    if (now.isAfter(flexibleStart)) {
                        // Task is overdue. Move it to NOW + buffer.
                        // We set it to essentially 'now' so rescheduleOverlaps can handle the flow
                        val duration = Duration.between(start, end).toMinutes()
                        val newStart = now.plusMinutes(5) 
                        val newEnd = newStart.plusMinutes(duration)
                        
                        // Check drop-dead threshold
                         if (task.thresholdTime != null && now.toLocalTime().isAfter(task.thresholdTime)) {
                             // Too late
                             // taskRepository.updateTask(task.copy(isSkipped = true)) // Optional: Skip instead of reschedule?
                         } else {
                             taskRepository.updateTask(task.copy(startTime = newStart, endTime = newEnd))
                             scheduleAlarms(task.copy(startTime = newStart, endTime = newEnd))
                             hasChanges = true
                         }
                    }
                }
            
            // 2. If we moved anything, or if there were existing overlaps, resolve them now.
            if (hasChanges || activeTasks.isNotEmpty()) {
                rescheduleOverlaps()
            }
        }
    }

    /**
     * Resolves schedule conflicts by pushing overlapping 'autoReschedule' tasks forward.
     * Preserves the schedule of fixed (non-autoReschedule) tasks where possible.
     */
    private suspend fun rescheduleOverlaps() {
        val tasks = taskRepository.getAllActiveTasksSync()
            .filter { !it.isCompleted && !it.isSkipped && it.startTime != null && it.endTime != null }
            .sortedBy { it.startTime } // Sort by start time.
        
        if (tasks.isEmpty()) return

        var previousEndTime = tasks[0].endTime!!
        // We start checking from the second task
        for (i in 1 until tasks.size) {
            val task = tasks[i]
            val buffer = 5L // 5 minutes buffer
            val minStart = previousEndTime.plusMinutes(buffer)
            
            if (task.startTime!!.isBefore(minStart)) {
                // OVERLAP DETECTED
                if (task.autoReschedule) {
                    // Move this task to resolve conflict
                    val duration = Duration.between(task.startTime!!, task.endTime!!).toMinutes()
                    val newStart = minStart
                    val newEnd = newStart.plusMinutes(duration)
                    
                     // Update DB
                    val updated = task.copy(startTime = newStart, endTime = newEnd)
                    taskRepository.updateTask(updated)
                    scheduleAlarms(updated)
                    
                    previousEndTime = newEnd
                } else {
                    // Cannot move this task (it's fixed). 
                    // It becomes the new anchor, potentially overlapping the *previous* one.
                    // Note: Since we processed the previous one already, we don't move it back.
                    // This task effectively 'wins' the slot in our simplified greedy algo.
                    previousEndTime = task.endTime!!
                }
            } else {
                // No overlap, just update pointer
                previousEndTime = task.endTime!!
            }
        }
    }

    fun cascadeTasks() {
        // "Cascade" -> Reschedule mundane tasks sequentially, respecting fixed events.
        viewModelScope.launch {
            val sortedTasks = taskRepository.getAllActiveTasksSync()
                .filter { it.startTime != null && it.endTime != null && !it.isCompleted && !it.isSkipped }
                .sortedBy { it.startTime }
            
            var currentTime = LocalDateTime.now().withSecond(0).withNano(0).plusMinutes(15)
            
            sortedTasks.forEach { task ->
                val duration = Duration.between(task.startTime!!, task.endTime!!).toMinutes()
                
                // CRITERIA FOR FIXED TASKS:
                // 1. Recurring tasks (e.g. Meetings, Classes)
                // 2. Goals (Important milestones)
                // 3. User specifically unchecked 'Auto Reschedule' (Manual fixed time)
                val isFixed = task.recurrence != com.example.lifeos.data.local.entity.Recurrence.NONE || 
                              task.isGoal || 
                              !task.autoReschedule
                
                if (isFixed) {
                   // This task stays put.
                   // However, if the cascade 'currentTime' has already surpassed this task's start,
                   // ideally we shouldn't move it if it's fixed. Use its End Time as the new floor for subsequent tasks.
                   val taskEnd = task.endTime!!
                   if (taskEnd.isAfter(currentTime)) {
                       currentTime = taskEnd.plusMinutes(5)
                   }
                   // We do NOT update this task in the DB.
                } else {
                    // This is a mundane task (e.g. "Brush teeth", "Read"). Cascade it.
                    // Ensure we don't schedule it over a future fixed task? 
                    // For simple cascade, we just push it to 'currentTime'.
                    
                    // Optimization: Check if 'currentTime' overlaps with next fixed task?
                    // For now, simple greedy push.
                    val newStart = currentTime
                    val newEnd = newStart.plusMinutes(duration)
                    
                    taskRepository.updateTask(task.copy(startTime = newStart, endTime = newEnd))
                    scheduleAlarms(task.copy(startTime = newStart, endTime = newEnd))
                    
                    currentTime = newEnd.plusMinutes(5)
                }
            }
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
            alarmScheduler.cancelAlarm(task.id)
            alarmScheduler.cancelAlarm(task.id + 100000)
        }
    }
    
    private fun scheduleAlarms(task: TaskEntity) {
        task.endTime?.let { endTime ->
            if (endTime.isAfter(LocalDateTime.now())) {
                alarmScheduler.scheduleAlarm(endTime, task.id, "Task Due: ${task.title}", "Your task is due now!")
                val reminderTime = endTime.minusMinutes(30)
                if (reminderTime.isAfter(LocalDateTime.now())) {
                    alarmScheduler.scheduleAlarm(reminderTime, task.id + 100000, "Task Reminder: ${task.title}", "Task is due in 30 minutes.")
                }
            }
        }
    }
}
