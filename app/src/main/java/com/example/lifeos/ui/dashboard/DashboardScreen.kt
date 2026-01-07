package com.example.lifeos.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.ui.AppViewModelProvider
import com.example.lifeos.ui.components.TaskCard
import com.example.lifeos.ui.dashboard.ScheduleMessageDialog

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.state.collectAsState()
    var showMessageDialog by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var showInfoDialog by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    val hour = java.time.LocalTime.now().hour
    val greeting = when (hour) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Welcoming Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = greeting,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Your Overview",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                androidx.compose.material3.IconButton(
                    onClick = { showInfoDialog = true },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                ) {
                    Icon(Icons.Filled.Info, contentDescription = "App Info", tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            DailySummaryCard(state.waterIntakeToday)
        }

        if (state.upcomingBills.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Upcoming Payment", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                            val bill = state.upcomingBills.first()
                            Text("${bill.description} due in ${java.time.Duration.between(java.time.LocalDateTime.now(), bill.dueDate).toDays()} days", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (state.doNow.isNotEmpty()) {
            item { SectionHeader("Active Now", MaterialTheme.colorScheme.primary) }
            items(items = state.doNow) { task ->
                TaskCard(task = task)
            }
        }

        if (state.upNext.isNotEmpty()) {
            item { SectionHeader("Coming Up", MaterialTheme.colorScheme.secondary) }
            items(items = state.upNext) { task ->
                TaskCard(task = task)
            }
        }

        if (state.laterToday.isNotEmpty()) {
            item { SectionHeader("Later", Color.Gray) }
            items(items = state.laterToday) { task ->
                TaskCard(task = task)
            }
        }
        
        if (state.upcomingEvents.isNotEmpty()) {
            item { SectionHeader("Events", MaterialTheme.colorScheme.tertiary) }
            items(state.upcomingEvents) { event ->
                com.example.lifeos.ui.events.EventCard(event = event, onClick = {})
            }
        }

        if (state.doNow.isEmpty() && state.upNext.isEmpty() && state.laterToday.isEmpty() && state.upcomingEvents.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "✨",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "All caught up for now.\nEnjoy your day!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }
    }

    if (showMessageDialog) {
        ScheduleMessageDialog(
            onDismiss = { showMessageDialog = false },
            onConfirm = { phone, msg, minutes, platform ->
                viewModel.scheduleMessage(phone, msg, minutes, platform)
            }
        )
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = { Text("Welcome to LifeOS") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Your all-in-one personal operating system.", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    AppFeatureItem("📅 Smart Tasks", "Auto-rescheduling, sequential cascading, and recurring activity management.")
                    AppFeatureItem("💧 Health Tracking", "Monitor your daily water intake directly from the dashboard or widget.")
                    AppFeatureItem("🌙 Focus Modes", "Protect your time with custom focus modes and auto-reply for callers.")
                    AppFeatureItem("💰 Finance", "Track expenses, manage bills, and split costs with people.")
                    AppFeatureItem("🔒 Vault", "Securely store sensitive notes and images with encryption.")
                    AppFeatureItem("✉️ Auto-Messaging", "Schedule WhatsApp or SMS messages for later.")
                }
            },
            confirmButton = { Button(onClick = { showInfoDialog = false }) { Text("Awesome!") } }
        )
    }
}

@Composable
fun AppFeatureItem(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun SectionHeader(title: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp, 12.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun DailySummaryCard(waterCount: Int) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        )
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.background(gradient).padding(24.dp)) {
            Column {
                Text(
                    "DAILY PROGRESS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            "Water Balance",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "$waterCount",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "milliliters",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        )
                    }
                    
                    // Simple radial-like indicator placeholder
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                         Text("💧", fontSize = 24.sp)
                    }
                }
            }
        }
    }
}


