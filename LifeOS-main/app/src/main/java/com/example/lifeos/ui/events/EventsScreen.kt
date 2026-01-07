package com.example.lifeos.ui.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.data.local.entity.EventEntity
import com.example.lifeos.data.local.entity.EventType
import com.example.lifeos.ui.AppViewModelProvider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.ui.draw.scale


import com.example.lifeos.data.local.entity.MessagePlatform
import com.example.lifeos.data.local.entity.MessageStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    viewModel: EventsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val events by viewModel.allEvents.collectAsState()
    val scheduledMessages by viewModel.allScheduledMessages.collectAsState()
    
    var showAddEventDialog by remember { mutableStateOf(false) }
    var showScheduleMsgDialog by remember { mutableStateOf(false) }
    var showEditEventDialog by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<EventEntity?>(null) }
    var selectedMessage by remember { mutableStateOf<com.example.lifeos.data.local.entity.ScheduledMessageEntity?>(null) }
    var showEditMessageDialog by remember { mutableStateOf(false) }
    
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Events", "Scheduled Messages")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Events & Scheduler") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    if (selectedTab == 0) showAddEventDialog = true 
                    else showScheduleMsgDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        
        if (selectedTab == 0) {
            // Events List
            val now = LocalDateTime.now()
            // Events separation
            // For recursive events, 'date' is usually future (next occurrence). So they naturally stay in upcoming unless we check differently.
            // Non-recursive past events go to past.
            val upcomingEvents = events.filter { it.date.isAfter(now) || (it.date.toLocalDate().isEqual(now.toLocalDate())) }.sortedBy { it.date }
            val pastEvents = events.filter { it.date.isBefore(now) && !it.date.toLocalDate().isEqual(now.toLocalDate()) }.sortedByDescending { it.date }

            LazyColumn(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                 if (upcomingEvents.isNotEmpty()) {
                    item { Text("Upcoming", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
                    items(items = upcomingEvents) { event ->
                        EventCard(event = event, onClick = { selectedEvent = event; showEditEventDialog = true })
                    }
                 }
                 
                 if (pastEvents.isNotEmpty()) {
                    item { Text("Past", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary) }
                    items(items = pastEvents) { event ->
                        EventCard(event = event, onClick = { selectedEvent = event; showEditEventDialog = true })
                    }
                 }
                 
                 if (upcomingEvents.isEmpty() && pastEvents.isEmpty()) {
                        item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            Text("No events", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                 }
            }
        } else {
            // Scheduled Messages List
            val now = LocalDateTime.now()
            val upcoming = scheduledMessages.filter { it.status == MessageStatus.PENDING && it.scheduledTime.isAfter(now) }.sortedBy { it.scheduledTime }
            val past = scheduledMessages.filter { it.status != MessageStatus.PENDING || it.scheduledTime.isBefore(now) }.sortedByDescending { it.scheduledTime }
            
            LazyColumn(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (upcoming.isNotEmpty()) {
                    item {
                        Text("Upcoming", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    items(upcoming) { msg ->
                        ScheduledMessageCard(msg, onClick = { selectedMessage = msg; showEditMessageDialog = true })
                    }
                }
                
                if (past.isNotEmpty()) {
                    item {
                        Text("Past", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                    }
                    items(past) { msg ->
                        ScheduledMessageCard(msg, onClick = { selectedMessage = msg; showEditMessageDialog = true })
                    }
                }
                
                if (upcoming.isEmpty() && past.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            Text("No scheduled messages", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
        
        if (showEditMessageDialog && selectedMessage != null) {
            EditMessageDialog(
                msg = selectedMessage!!,
                onDismiss = { showEditMessageDialog = false; selectedMessage = null },
                onConfirm = { updatedMsg ->
                    viewModel.updateScheduledMessage(updatedMsg)
                    showEditMessageDialog = false
                    selectedMessage = null
                }
            )
        }


        if (showAddEventDialog) {
            AddEventDialog(
                onDismiss = { showAddEventDialog = false },
                onConfirm = { title, type, date, isRecursive, message, contact, category, description, platform ->
                    viewModel.addEvent(
                        EventEntity(
                            title = title,
                            type = type,
                            date = date,
                            reminderMinutes = 1440,
                            isRecursive = isRecursive,
                            scheduledMessageBody = message,
                            scheduledMessageContact = contact,
                            customCategory = category,
                            description = description,
                            scheduledMessagePlatform = platform
                        )
                    )
                    showAddEventDialog = false
                }
            )
        }

        if (showEditEventDialog && selectedEvent != null) {
            EditEventDialog(
                event = selectedEvent!!,
                onDismiss = { showEditEventDialog = false; selectedEvent = null },
                onConfirm = { updated ->
                    viewModel.updateEvent(updated)
                    showEditEventDialog = false
                    selectedEvent = null
                },
                onDelete = { toDelete ->
                    viewModel.deleteEvent(toDelete)
                    showEditEventDialog = false
                    selectedEvent = null
                }
            )
        }
    }
    
    if (showScheduleMsgDialog) {
        com.example.lifeos.ui.dashboard.ScheduleMessageDialog(
            onDismiss = { showScheduleMsgDialog = false },
            onConfirm = { phone, msg, minutes, platform ->
                viewModel.scheduleMessage(phone, msg, minutes, platform)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduledMessageCard(
    msg: com.example.lifeos.data.local.entity.ScheduledMessageEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(msg.contactNumber, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(
                    msg.scheduledTime.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")), 
                    style = MaterialTheme.typography.bodySmall, 
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                 AssistChip(
                     onClick = { },
                     label = { Text(if(msg.platform == MessagePlatform.WHATSAPP) "WhatsApp" else "SMS") },
                     modifier = Modifier.scale(0.8f) 
                 )
                 Spacer(modifier = Modifier.width(8.dp))
                     Text(
                         text = msg.status.name,
                         style = MaterialTheme.typography.labelSmall,
                         color = when(msg.status) {
                             MessageStatus.SENT -> Color.Green
                             MessageStatus.FAILED -> Color.Red
                             MessageStatus.PENDING -> if(msg.scheduledTime.isBefore(java.time.LocalDateTime.now())) Color.Red else Color.Gray
                         }
                     )
            }
            Text(msg.messageBody, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMessageDialog(
    msg: com.example.lifeos.data.local.entity.ScheduledMessageEntity,
    onDismiss: () -> Unit,
    onConfirm: (com.example.lifeos.data.local.entity.ScheduledMessageEntity) -> Unit
) {
    var messageBody by remember { mutableStateOf(msg.messageBody) }
    var contactNumber by remember { mutableStateOf(msg.contactNumber) }
    var platform by remember { mutableStateOf(msg.platform) }
    // Recalculate time? Assuming user wants to change content mostly. If time edit needed, add picker.
    // For simplicity, just content/platform/contact.

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Message") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = contactNumber, 
                    onValueChange = { contactNumber = it }, 
                    label = { Text("To (Phone)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = messageBody,
                    onValueChange = { messageBody = it },
                    label = { Text("Message Body") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Platform: ")
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = platform == MessagePlatform.WHATSAPP,
                        onClick = { platform = MessagePlatform.WHATSAPP },
                        label = { Text("WhatsApp") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = platform == MessagePlatform.SMS,
                        onClick = { platform = MessagePlatform.SMS },
                        label = { Text("SMS") }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { 
                onConfirm(msg.copy(
                    messageBody = messageBody,
                    contactNumber = contactNumber, 
                    platform = platform
                )) 
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun EventCard(event: EventEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarToday,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(event.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                
                val typeLabel = if (event.type == com.example.lifeos.data.local.entity.EventType.OTHER && !event.customCategory.isNullOrBlank()) {
                    event.customCategory
                } else {
                    event.type.name
                }
                
                Text(
                    text = "$typeLabel • ${event.date.format(DateTimeFormatter.ofPattern("MMM dd, hh:mm a"))}${if(event.isRecursive) " (Yearly)" else ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Countdown logic
                val now = LocalDateTime.now()
                // For recursive events, we've likely updated the 'date' field to next occurrence in the logic
                // But if it's stored as past date but marked recursive... 
                // The current AddEvent logic sets it to next occurrence if past.
                // Re-calculating next occurrence just in case for display:
                var target = event.date
                if (event.isRecursive && target.isBefore(now)) {
                    // Logic to find next anniversary
                     target = target.withYear(now.year)
                     if (target.isBefore(now)) {
                         target = target.plusYears(1)
                     }
                }
                
                val daysLeft = java.time.temporal.ChronoUnit.DAYS.between(now.toLocalDate(), target.toLocalDate())
                val displayTime = if (daysLeft == 0L) {
                    "Today"
                } else if (daysLeft == 1L) {
                    "Tomorrow"
                } else {
                    "$daysLeft days to go"
                }

                Text(
                    text = displayTime,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if(daysLeft <= 3) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )

                if (event.scheduledMessageBody != null) {
                    Text(
                        "Has Scheduled Message",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, EventType, LocalDateTime, Boolean, String?, String?, String?, String?, MessagePlatform?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(EventType.BIRTHDAY) }
    var customCategory by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("1") }
    var month by remember { mutableStateOf("1") }
    var eventTime by remember { mutableStateOf(java.time.LocalTime.of(9, 0)) }
    
    val timePickerDialog = android.app.TimePickerDialog(
        androidx.compose.ui.platform.LocalContext.current,
        { _, h, m -> eventTime = java.time.LocalTime.of(h, m) },
        eventTime.hour,
        eventTime.minute,
        false
    )
    
    // New Feature States
    var isRecursive by remember { mutableStateOf(false) }
    var isMessageEnabled by remember { mutableStateOf(false) }
    var messageBody by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf(MessagePlatform.WHATSAPP) }
    
    val context = androidx.compose.ui.platform.LocalContext.current
    val contactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri: android.net.Uri? ->
        uri?.let {
            val num = com.example.lifeos.ui.util.getPhoneNumberFromUri(context, it)
            if (num != null) {
                contactNumber = num
            } else {
                Toast.makeText(context, "Failed to get number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Event") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(
                     value = description,
                     onValueChange = { description = it },
                     label = { Text("Description (Optional)") },
                     maxLines = 3,
                     modifier = Modifier.fillMaxWidth()
                )
                
                Text("Type")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EventType.values().forEach { t ->
                        FilterChip(
                            selected = type == t,
                            onClick = { type = t },
                            label = { Text(t.name.first() + t.name.substring(1).lowercase()) }
                        )
                    }
                }
                
                if (type == EventType.OTHER) {
                    OutlinedTextField(
                        value = customCategory,
                        onValueChange = { customCategory = it },
                        label = { Text("Category Name (e.g. Work)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                     OutlinedTextField(
                         value = day, 
                         onValueChange = { day = it }, 
                         label = { Text("Day") },
                         modifier = Modifier.weight(1f)
                     )
                     OutlinedTextField(
                         value = month, 
                         onValueChange = { month = it }, 
                         label = { Text("Month") },
                         modifier = Modifier.weight(1f)
                     )
                }
                
                Button(
                    onClick = { timePickerDialog.show() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Time: ${eventTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}")
                }
                
                // Recurrence
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Repeat Yearly?", modifier = Modifier.weight(1f))
                    Switch(checked = isRecursive, onCheckedChange = { isRecursive = it })
                }
                
                Divider()
                
                // Message Scheduler
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Schedule Message?", modifier = Modifier.weight(1f))
                    Switch(checked = isMessageEnabled, onCheckedChange = { isMessageEnabled = it })
                }
                
                if (isMessageEnabled) {
                     Row(verticalAlignment = Alignment.CenterVertically) {
                         Text("Platform:")
                         Spacer(modifier = Modifier.width(8.dp))
                         FilterChip(selected = platform == MessagePlatform.WHATSAPP, onClick = { platform = MessagePlatform.WHATSAPP }, label = { Text("WhatsApp") })
                         Spacer(modifier = Modifier.width(8.dp))
                         FilterChip(selected = platform == MessagePlatform.SMS, onClick = { platform = MessagePlatform.SMS }, label = { Text("SMS") })
                     }

                     Row(verticalAlignment = Alignment.CenterVertically) {
                         OutlinedTextField(
                             value = contactNumber, 
                             onValueChange = { contactNumber = it }, 
                             label = { Text("To (Phone)") },
                             modifier = Modifier.weight(1f)
                         )
                         IconButton(onClick = { contactLauncher.launch(null) }) {
                             Icon(Icons.Filled.Contacts, "Pick Contact")
                         }
                     }
                     OutlinedTextField(
                         value = messageBody,
                         onValueChange = { messageBody = it },
                         label = { Text("Message Body") },
                         maxLines = 3,
                         modifier = Modifier.fillMaxWidth()
                     )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val d = day.toIntOrNull()
                val m = month.toIntOrNull()
                if (title.isNotBlank() && d != null && m != null) {
                    try {
                        val currentYear = LocalDateTime.now().year
                        var date = LocalDateTime.of(currentYear, m, d, eventTime.hour, eventTime.minute)
                        if (date.isBefore(LocalDateTime.now())) {
                            date = date.plusYears(1)
                        }
                        
                        onConfirm(
                            title, 
                            type, 
                            date, 
                            isRecursive, 
                            if(isMessageEnabled) messageBody else null, 
                            if(isMessageEnabled) contactNumber else null,
                            if(type == EventType.OTHER) customCategory else null,
                            if(description.isNotBlank()) description else null,
                            if(isMessageEnabled) platform else null
                        )
                    } catch (e: Exception) {
                        Toast.makeText(context, "Invalid Date", Toast.LENGTH_SHORT).show()
                    }
                }
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventDialog(
    event: EventEntity,
    onDismiss: () -> Unit,
    onConfirm: (EventEntity) -> Unit,
    onDelete: (EventEntity) -> Unit
) {
    var title by remember { mutableStateOf(event.title) }
    var description by remember { mutableStateOf(event.description ?: "") }
    var isRecursive by remember { mutableStateOf(event.isRecursive) }
    var messageBody by remember { mutableStateOf(event.scheduledMessageBody ?: "") }
    var contactNumber by remember { mutableStateOf(event.scheduledMessageContact ?: "") }
     var isMessageEnabled by remember { mutableStateOf(event.scheduledMessageBody != null) }
    var platform by remember { mutableStateOf(event.scheduledMessagePlatform ?: MessagePlatform.WHATSAPP) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Event") },
        text = {
             Column(
                 verticalArrangement = Arrangement.spacedBy(16.dp),
                 modifier = Modifier.verticalScroll(rememberScrollState())
             ) {
                 OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                 
                 OutlinedTextField(
                     value = description,
                     onValueChange = { description = it },
                     label = { Text("Description (Optional)") },
                     maxLines = 3,
                     modifier = Modifier.fillMaxWidth()
                 )

                 Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Repeat Yearly?", modifier = Modifier.weight(1f))
                    Switch(checked = isRecursive, onCheckedChange = { isRecursive = it })
                 }

                 Divider()
                 
                 Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Schedule Message?", modifier = Modifier.weight(1f))
                    Switch(checked = isMessageEnabled, onCheckedChange = { isMessageEnabled = it })
                 }
                
                 if (isMessageEnabled) {
                     val context = androidx.compose.ui.platform.LocalContext.current
                     val contactLauncher = rememberLauncherForActivityResult(
                         contract = ActivityResultContracts.PickContact()
                     ) { uri: android.net.Uri? ->
                         uri?.let {
                             val num = com.example.lifeos.ui.util.getPhoneNumberFromUri(context, it)
                             if (num != null) contactNumber = num
                         }
                     }

                     Row(verticalAlignment = Alignment.CenterVertically) {
                         Text("Platform:")
                         Spacer(modifier = Modifier.width(8.dp))
                         FilterChip(selected = platform == MessagePlatform.WHATSAPP, onClick = { platform = MessagePlatform.WHATSAPP }, label = { Text("WhatsApp") })
                         Spacer(modifier = Modifier.width(8.dp))
                         FilterChip(selected = platform == MessagePlatform.SMS, onClick = { platform = MessagePlatform.SMS }, label = { Text("SMS") })
                     }

                     Row(verticalAlignment = Alignment.CenterVertically) {
                         OutlinedTextField(
                             value = contactNumber, 
                             onValueChange = { contactNumber = it }, 
                             label = { Text("To (Phone)") },
                             modifier = Modifier.weight(1f)
                         )
                         IconButton(onClick = { contactLauncher.launch(null) }) {
                             Icon(Icons.Filled.Contacts, "Pick Contact")
                         }
                     }
                     OutlinedTextField(
                         value = messageBody,
                         onValueChange = { messageBody = it },
                         label = { Text("Message Body") },
                         maxLines = 3,
                         modifier = Modifier.fillMaxWidth()
                     )
                 }
             }
        },
        confirmButton = {
            Button(onClick = { 
                onConfirm(event.copy(
                    title = title,
                    isRecursive = isRecursive,
                    description = description,
                    scheduledMessageBody = if(isMessageEnabled) messageBody else null,
                    scheduledMessageContact = if(isMessageEnabled) contactNumber else null,
                    scheduledMessagePlatform = if(isMessageEnabled) platform else null
                )) 
            }) { Text("Save") }
        },
        dismissButton = {
            Row {
                TextButton(onClick = { onDelete(event) }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Delete") }
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        }
    )
}
