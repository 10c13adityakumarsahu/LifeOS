package com.example.lifeos.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.local.entity.EventEntity
import com.example.lifeos.data.repository.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import java.time.LocalDateTime


// ViewModel for managing Events and Scheduled Messages
class EventsViewModel(
    private val eventRepository: EventRepository,
    private val scheduledMessageRepository: com.example.lifeos.data.repository.ScheduledMessageRepository,
    private val alarmScheduler: com.example.lifeos.data.manager.AlarmScheduler
) : ViewModel() {
    val allEvents: StateFlow<List<EventEntity>> = eventRepository.allEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allScheduledMessages = scheduledMessageRepository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEvent(event: EventEntity) {
        viewModelScope.launch {
            // Check if message should be scheduled
            var messageId: Long? = null
            if (event.scheduledMessageBody != null && event.scheduledMessageContact != null) {
                 val platform = event.scheduledMessagePlatform ?: com.example.lifeos.data.local.entity.MessagePlatform.WHATSAPP
                 val msgEntity = com.example.lifeos.data.local.entity.ScheduledMessageEntity(
                    contactName = "Event Contact", 
                    contactNumber = event.scheduledMessageContact,
                    messageBody = event.scheduledMessageBody,
                    scheduledTime = event.date,
                    platform = platform
                )
                messageId = scheduledMessageRepository.scheduleMessage(msgEntity)
                
                alarmScheduler.scheduleMessageAlarm(
                    event.date,
                    messageId.toInt(),
                    event.scheduledMessageContact,
                    event.scheduledMessageBody,
                    platform.name
                )
            }
            
            // Add event with linked ID
            eventRepository.addEvent(event.copy(scheduledMessageId = messageId))
        }
    }

    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            // 1. Handle Linked Message
            // Case A: Message was enabled, and still is. Update logic.
            // Case B: Message was enabled, now disabled. Delete logic.
            // Case C: Message was disabled, now enabled. Create logic.
            
            var newMessageId = event.scheduledMessageId
            
            val isMessageEnabled = event.scheduledMessageBody != null && event.scheduledMessageContact != null
            val platform = event.scheduledMessagePlatform ?: com.example.lifeos.data.local.entity.MessagePlatform.WHATSAPP
            
            if (isMessageEnabled) {
                if (event.scheduledMessageId != null) {
                    // Update existing
                    val existingMsg = scheduledMessageRepository.getMessageById(event.scheduledMessageId.toInt())
                    if (existingMsg != null) {
                         val updatedMsg = existingMsg.copy(
                             contactNumber = event.scheduledMessageContact!!,
                             messageBody = event.scheduledMessageBody!!,
                             scheduledTime = event.date,
                             platform = platform,
                             // Reset status if time changed? For now, keep it simple. If edited, maybe reset to pending.
                             status = com.example.lifeos.data.local.entity.MessageStatus.PENDING
                         )
                         scheduledMessageRepository.updateMessage(updatedMsg)
                         alarmScheduler.scheduleMessageAlarm(
                             event.date,
                             updatedMsg.id,
                             updatedMsg.contactNumber,
                             updatedMsg.messageBody,
                             platform.name
                         )
                    } else {
                        // ID exists but msg gone? Re-create
                         val msgEntity = com.example.lifeos.data.local.entity.ScheduledMessageEntity(
                            contactName = "Event Contact", 
                            contactNumber = event.scheduledMessageContact!!,
                            messageBody = event.scheduledMessageBody!!,
                            scheduledTime = event.date,
                            platform = platform
                        )
                        newMessageId = scheduledMessageRepository.scheduleMessage(msgEntity)
                        alarmScheduler.scheduleMessageAlarm(event.date, newMessageId.toInt(), event.scheduledMessageContact!!, event.scheduledMessageBody!!, platform.name)
                    }
                } else {
                    // Create new
                    val msgEntity = com.example.lifeos.data.local.entity.ScheduledMessageEntity(
                        contactName = "Event Contact", 
                        contactNumber = event.scheduledMessageContact!!,
                        messageBody = event.scheduledMessageBody!!,
                        scheduledTime = event.date,
                        platform = platform
                    )
                    newMessageId = scheduledMessageRepository.scheduleMessage(msgEntity)
                    alarmScheduler.scheduleMessageAlarm(event.date, newMessageId.toInt(), event.scheduledMessageContact!!, event.scheduledMessageBody!!, platform.name)
                }
            } else {
                // Disabled
                if (event.scheduledMessageId != null) {
                    // Delete existing
                    // We need dummy entity or deleteById
                     val existingMsg = scheduledMessageRepository.getMessageById(event.scheduledMessageId.toInt())
                     if (existingMsg != null) {
                         scheduledMessageRepository.deleteMessage(existingMsg)
                         // Cancel alarm (requires re-creating intent specific to that ID)
                         // AlarmScheduler needs cancelMessageAlarm method ideally or reuse logic.
                         // But for now, we can try to "overwrite" it or just leave it (it will fail/ignore if DB missing).
                         // Actually, we should cancel.
                         // Let's assume alarmScheduler has cancelMessageAlarm or I can add it?
                         // I'll add cancelMessageAlarm to AlarmScheduler later or now.
                         // Just use existing cancelAlarm? No, different intent class.
                         // I will call `alarmScheduler.cancelMessageAlarm(id)` - I need to add this method.
                         alarmScheduler.cancelMessageAlarm(event.scheduledMessageId.toInt())
                     }
                    newMessageId = null
                }
            }
            
            eventRepository.updateEvent(event.copy(scheduledMessageId = newMessageId))
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.deleteEvent(event)
        }
    }

    fun scheduleMessage(phone: String, message: String, minutesFromNow: Int, platform: com.example.lifeos.data.local.entity.MessagePlatform) {
        viewModelScope.launch {
            val scheduledTime = LocalDateTime.now().plusMinutes(minutesFromNow.toLong())
            val msgEntity = com.example.lifeos.data.local.entity.ScheduledMessageEntity(
                contactName = "Contact", 
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
    
    fun updateScheduledMessage(msg: com.example.lifeos.data.local.entity.ScheduledMessageEntity) {
        viewModelScope.launch {
            scheduledMessageRepository.updateMessage(msg)
            // Re-schedule alarm
            alarmScheduler.scheduleMessageAlarm(
                 msg.scheduledTime,
                 msg.id,
                 msg.contactNumber,
                 msg.messageBody,
                 msg.platform.name
            )
        }
    }
}
