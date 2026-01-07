package com.example.lifeos.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.data.local.entity.Priority
import com.example.lifeos.data.local.entity.TaskEntity
import com.example.lifeos.data.local.entity.TaskCategory
import com.example.lifeos.data.local.entity.Recurrence
import com.example.lifeos.ui.AppViewModelProvider
import com.example.lifeos.ui.components.TaskCard
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val allTasks by viewModel.allTasks.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Scheduled", "Backlog", "Goals")

    val filteredTasks = when (selectedTab) {
        0 -> allTasks.filter { !it.isGoal && it.startTime != null }
        1 -> allTasks.filter { !it.isGoal && it.startTime == null }
        2 -> allTasks.filter { it.isGoal }
        else -> allTasks
    }

    val activeTasks = filteredTasks.filter { !it.isCompleted && !it.isSkipped }
    val pastTasks = filteredTasks.filter { it.isCompleted || it.isSkipped }
    
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showEditTaskDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<TaskEntity?>(null) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.refreshSmartSchedule()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasks") },
                actions = {
                    androidx.compose.material3.IconButton(onClick = { showInfoDialog = true }) {
                        Icon(Icons.Filled.Info, contentDescription = "Features Info", tint = MaterialTheme.colorScheme.primary)
                    }
                    if (selectedTab == 0) {
                        androidx.compose.material3.IconButton(onClick = { viewModel.cascadeTasks() }) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = "Cascade", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { showAddTaskDialog = true }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            androidx.compose.material3.TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    androidx.compose.material3.Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (selectedTab == 0 && activeTasks.isNotEmpty()) {
                    item {
                        Button(
                            onClick = { viewModel.cascadeTasks() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Cascade Overdue Tasks")
                        }
                    }
                }

                item {
                    Text("Active", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                
                if (activeTasks.isEmpty()) {
                    item {
                        Text("No active items", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                } else {
                    items(items = activeTasks) { task ->
                        TaskCard(
                            task = task,
                            onCheckedChange = { isChecked ->
                                viewModel.toggleTaskCompletion(task, isChecked)
                            },
                            onClick = {
                                selectedTask = task
                                showEditTaskDialog = true
                            }
                        )
                    }
                }

                if (pastTasks.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Completed", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                    items(items = pastTasks) { task ->
                        TaskCard(
                            task = task,
                            onCheckedChange = { isChecked ->
                                viewModel.toggleTaskCompletion(task, isChecked)
                            },
                            onClick = {
                                selectedTask = task
                                showEditTaskDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
    
    // ... (EditTaskDialog remains similar but we might need to update it later if user wants to edit these fields)
    // For brevity, skipping full EditTaskDialog overhaul unless requested, focusing on Add.
    
    if (showEditTaskDialog && selectedTask != null) {
        EditTaskDialog(
            task = selectedTask!!,
            onDismiss = { showEditTaskDialog = false; selectedTask = null },
            onConfirm = { updatedTask ->
                viewModel.updateTask(updatedTask)
                showEditTaskDialog = false
                selectedTask = null
            },
            onDelete = { taskToDelete ->
                viewModel.deleteTask(taskToDelete)
                showEditTaskDialog = false
                selectedTask = null
            }
        )
    }

    if (showAddTaskDialog) {
        AddTaskDialog(
            onDismiss = { showAddTaskDialog = false },
            onConfirm = { title, desc, priority, startTime, endTime, category, recurrence, autoReschedule, isGoal ->
                viewModel.addTask(
                    title = title,
                    description = desc,
                    start = startTime,
                    end = endTime,
                    priority = priority,
                    category = category,
                    recurrence = recurrence,
                    flexibility = 30,
                    duration = 60,
                    autoReschedule = autoReschedule,
                    thresholdTime = if (category == TaskCategory.HABIT) java.time.LocalTime.of(11, 0) else null,
                    isGoal = isGoal
                )
                showAddTaskDialog = false
            }
        )
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = { Text("LifeOS Task Features") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    FeatureListItem("Auto-Reschedule", "Smartly moves missed tasks to the next available slot based on your flexibility.")
                    FeatureListItem("Cascade Overdue", "Re-arranges all your overdue tasks sequentially starting from now.")
                    FeatureListItem("Recurring Activities", "Supports Daily, Weekly, and Monthly patterns. Spawns next instance automatically on completion.")
                    FeatureListItem("Smart Thresholds", "Habits can have 'drop-dead' times after which they are automatically skipped.")
                    FeatureListItem("Backlog & Goals", "Separate tabs for non-timed tasks and long-term objectives.")
                    FeatureListItem("Consolidated Widget", "Control focus mode, track water, and view tasks directly from home screen.")
                }
            },
            confirmButton = { Button(onClick = { showInfoDialog = false }) { Text("Got it") } }
        )
    }
}

@Composable
fun FeatureListItem(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(description, style = MaterialTheme.typography.bodySmall)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskDialog(
    task: TaskEntity,
    onDismiss: () -> Unit,
    onConfirm: (TaskEntity) -> Unit,
    onDelete: (TaskEntity) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var priority by remember { mutableStateOf(task.priority) }
    var category by remember { mutableStateOf(task.category) }
    var recurrence by remember { mutableStateOf(task.recurrence) }
    var autoReschedule by remember { mutableStateOf(task.autoReschedule) }
    var isGoal by remember { mutableStateOf(task.isGoal) }
    
    var startTime by remember { mutableStateOf(task.startTime ?: LocalDateTime.now()) }
    var endTime by remember { mutableStateOf(task.endTime ?: LocalDateTime.now().plusHours(1)) }
    var hasSchedule by remember { mutableStateOf(task.startTime != null || task.endTime != null) }

    var categoryExpanded by remember { mutableStateOf(false) }
    var recurrenceExpanded by remember { mutableStateOf(false) }
    
    val context = androidx.compose.ui.platform.LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Task") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())

                // Priority
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Priority.values().forEach { p ->
                        val selected = priority == p
                        val color = when(p) { Priority.CRITICAL -> MaterialTheme.colorScheme.error; Priority.HIGH -> MaterialTheme.colorScheme.tertiary; else -> MaterialTheme.colorScheme.primary }
                         Card(
                            colors = CardDefaults.cardColors(containerColor = if (selected) color else MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.clickable { priority = p }
                        ) {
                             Text(p.name.first().toString(), modifier = Modifier.padding(8.dp), color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                // Category
                ExposedDropdownMenuBox(expanded = categoryExpanded, onExpandedChange = { categoryExpanded = !categoryExpanded }) {
                    OutlinedTextField(
                        value = category.name, onValueChange = {}, readOnly = true, label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) {
                        TaskCategory.values().forEach { cat ->
                            DropdownMenuItem(text = { Text(cat.name) }, onClick = { category = cat; categoryExpanded = false })
                        }
                    }
                }

                // Recurrence
                ExposedDropdownMenuBox(expanded = recurrenceExpanded, onExpandedChange = { recurrenceExpanded = !recurrenceExpanded }) {
                    OutlinedTextField(
                        value = recurrence.name, onValueChange = {}, readOnly = true, label = { Text("Recurrence") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = recurrenceExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = recurrenceExpanded, onDismissRequest = { recurrenceExpanded = false }) {
                        Recurrence.values().forEach { r ->
                            DropdownMenuItem(text = { Text(r.name) }, onClick = { recurrence = r; recurrenceExpanded = false })
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Checkbox(checked = isGoal, onCheckedChange = { isGoal = it })
                    Text("Long Term Goal")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Checkbox(checked = hasSchedule, onCheckedChange = { hasSchedule = it })
                    Text("Is Scheduled")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Checkbox(checked = autoReschedule, onCheckedChange = { autoReschedule = it })
                    Text("Auto-Reschedule")
                }

                if (hasSchedule) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Start: ${startTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}")
                        TextButton(onClick = {
                            android.app.TimePickerDialog(context, { _, h, m -> startTime = startTime.withHour(h).withMinute(m) }, startTime.hour, startTime.minute, false).show()
                        }) { Text("Set") }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("End: ${endTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}")
                        TextButton(onClick = {
                            android.app.TimePickerDialog(context, { _, h, m -> endTime = endTime.withHour(h).withMinute(m) }, endTime.hour, endTime.minute, false).show()
                        }) { Text("Set") }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(task.copy(
                    title = title, description = description, priority = priority,
                    category = category, recurrence = recurrence, autoReschedule = autoReschedule,
                    isGoal = isGoal, startTime = if (hasSchedule) startTime else null,
                    endTime = if (hasSchedule) endTime else null
                ))
            }) { Text("Save") }
        },
        dismissButton = {
            Row {
                TextButton(onClick = { onDelete(task) }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Priority, LocalDateTime?, LocalDateTime?, TaskCategory, Recurrence, Boolean, Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var hasSchedule by remember { mutableStateOf(true) }
    var IsScheduledWindow by remember { mutableStateOf(false) }
    var isGoal by remember { mutableStateOf(false) }
    
    var startTime by remember { mutableStateOf(LocalDateTime.now()) }
    var dueTime by remember { mutableStateOf(LocalDateTime.now().plusHours(1)) }
    
    // New Fields
    var category by remember { mutableStateOf(TaskCategory.WORK) }
    var recurrence by remember { mutableStateOf(Recurrence.NONE) }
    var autoReschedule by remember { mutableStateOf(false) }
    
    // UI Helpers
    var categoryExpanded by remember { mutableStateOf(false) }
    var recurrenceExpanded by remember { mutableStateOf(false) }
    
    val context = androidx.compose.ui.platform.LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Task") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    singleLine = true
                )
                
                // Priority
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Priority.values().forEach { p ->
                        val selected = priority == p
                        val color = when(p) { Priority.CRITICAL -> MaterialTheme.colorScheme.error; Priority.HIGH -> MaterialTheme.colorScheme.tertiary; else -> MaterialTheme.colorScheme.primary }
                         Card(
                            colors = CardDefaults.cardColors(containerColor = if (selected) color else MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.clickable { priority = p }
                        ) {
                             Text(p.name.first().toString(), modifier = Modifier.padding(8.dp), color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                
                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = category.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        TaskCategory.values().forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name) },
                                onClick = { category = cat; categoryExpanded = false; if (cat == TaskCategory.HABIT) autoReschedule = true }
                            )
                        }
                    }
                }
                
                // Recurrence Dropdown
                 ExposedDropdownMenuBox(
                    expanded = recurrenceExpanded,
                    onExpandedChange = { recurrenceExpanded = !recurrenceExpanded }
                ) {
                    OutlinedTextField(
                        value = recurrence.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Recurrence") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = recurrenceExpanded) },
                         modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = recurrenceExpanded,
                        onDismissRequest = { recurrenceExpanded = false }
                    ) {
                        Recurrence.values().forEach { r ->
                            DropdownMenuItem(
                                text = { Text(r.name) },
                                onClick = { recurrence = r; recurrenceExpanded = false }
                            )
                        }
                    }
                }
                
                // Goal and Deadline Toggles
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Checkbox(checked = isGoal, onCheckedChange = { 
                        isGoal = it 
                        if (it) hasSchedule = false 
                    })
                    Text("Long Term Goal")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Checkbox(checked = hasSchedule, onCheckedChange = { hasSchedule = it })
                    Text("Is Scheduled")
                }

                if (hasSchedule) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        androidx.compose.material3.Checkbox(checked = IsScheduledWindow, onCheckedChange = { IsScheduledWindow = it })
                        Text("Add Specific Time Range")
                    }
                }

                // Auto Reschedule Check
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Checkbox(checked = autoReschedule, onCheckedChange = { autoReschedule = it })
                    Text("Auto-Reschedule (Smart)")
                }

                // Time Pickers
                if (hasSchedule) {
                    if (IsScheduledWindow) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("Start: ${startTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}")
                                TextButton(onClick = {
                                    android.app.TimePickerDialog(context, { _, h, m -> startTime = startTime.withHour(h).withMinute(m) }, startTime.hour, startTime.minute, false).show()
                                }) { Text("Set Start") }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("End: ${dueTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}")
                                TextButton(onClick = {
                                    android.app.TimePickerDialog(context, { _, h, m -> dueTime = dueTime.withHour(h).withMinute(m) }, dueTime.hour, dueTime.minute, false).show()
                                }) { Text("Set End") }
                            }
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Due: ${dueTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))}")
                            TextButton(onClick = {
                                android.app.TimePickerDialog(context, { _, h, m -> dueTime = dueTime.withHour(h).withMinute(m) }, dueTime.hour, dueTime.minute, false).show()
                            }) { Text("Set Clock") }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val s = if (hasSchedule && IsScheduledWindow) startTime else if (hasSchedule) LocalDateTime.now() else null
                        val e = if (hasSchedule) dueTime else null
                        onConfirm(title, description, priority, s, e, category, recurrence, autoReschedule, isGoal)
                    }
                }
            ) { Text("Create") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
