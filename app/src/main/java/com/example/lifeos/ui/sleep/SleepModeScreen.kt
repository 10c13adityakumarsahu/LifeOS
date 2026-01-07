package com.example.lifeos.ui.sleep

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.ui.AppViewModelProvider
import com.example.lifeos.ui.settings.SettingsViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifeos.ui.util.getPhoneNumberFromUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusModeScreen(
    onExitFocusMode: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val settingsState by viewModel.settings.collectAsState()
    val settings = settingsState ?: return
    val logs by viewModel.callbackLogs.collectAsState()
    
    val context = LocalContext.current
    
    val contactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri: android.net.Uri? ->
        uri?.let {
            val contactData = com.example.lifeos.ui.util.getContactDetailsFromUri(context, it)
            if (contactData != null) {
                viewModel.addSleepContact(contactData.first)
            } else {
                Toast.makeText(context, "Failed to get contact info", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    // Local state for configuration if not yet active
    var selectedMode by remember { mutableStateOf("Sleep") } // Default
    val isFocusActive = settings.currentFocusMode != "None"
    
    // Sync local state with active mode if active
    LaunchedEffect(settings.currentFocusMode) {
        if (settings.currentFocusMode != "None") {
            selectedMode = settings.currentFocusMode
        }
    }

    // Intercept Back Press if Active
    BackHandler(enabled = isFocusActive) {
        Toast.makeText(context, "Turn off Focus Mode to go back", Toast.LENGTH_SHORT).show()
    }

    // Full Screen Mode (Hide System Bars) if Active
    if (isFocusActive) {
        DisposableEffect(Unit) {
            val window = (context as? android.app.Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, window.decorView)
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
                insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                onDispose {
                    insetsController.show(WindowInsetsCompat.Type.systemBars())
                }
            } else {
                onDispose { }
            }
        }
    }
    
    val activeColor = when(selectedMode) {
        "Sleep" -> Color(0xFFBB86FC)
        "Driving" -> Color(0xFFFFA000)
        "Meeting" -> Color(0xFF03DAC6)
        else -> Color.White
    }
    
    val icon = when(selectedMode) {
        "Sleep" -> Icons.Filled.Bedtime
        "Driving" -> Icons.Filled.DirectionsCar
        "Meeting" -> Icons.Filled.Groups
        else -> Icons.Filled.Bedtime
    }

    // Very dark theme background
    Surface(
        color = Color(0xFF121212),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (isFocusActive) Arrangement.Center else Arrangement.Top
        ) {
            if (!isFocusActive) {
                 // Header
                 Text("Select Focus Mode", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                 Spacer(modifier = Modifier.height(24.dp))
                 
                 // Mode Selector
                 Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                     listOf("Sleep", "Driving", "Meeting").forEach { mode ->
                         FilterChip(
                             selected = selectedMode == mode,
                             onClick = { selectedMode = mode },
                             label = { Text(mode) },
                             leadingIcon = {
                                 Icon(
                                     when(mode) {
                                         "Sleep" -> Icons.Filled.Bedtime
                                         "Driving" -> Icons.Filled.DirectionsCar
                                         "Meeting" -> Icons.Filled.Groups
                                         else -> Icons.Filled.Bedtime
                                     },
                                     null
                                 )
                             },
                             colors = FilterChipDefaults.filterChipColors(
                                 selectedContainerColor = activeColor.copy(alpha=0.5f),
                                 selectedLabelColor = Color.White
                             )
                         )
                     }
                 }
                 Spacer(modifier = Modifier.height(24.dp))
                 
                 // Silent Mode Option
                 Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                     Icon(Icons.Filled.NotificationsOff, null, tint = Color.Gray)
                     Spacer(modifier = Modifier.width(16.dp))
                     Text("Silent Mode on Start", color = Color.White, modifier = Modifier.weight(1f))
                     Switch(
                         checked = settings.isSilentModeEnabledForFocus,
                         onCheckedChange = { viewModel.updateSilentModeForFocus(it) }
                     )
                 }
                 Spacer(modifier = Modifier.height(16.dp))

                 // Emergency Override Option
                 Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                     Icon(Icons.Filled.Call, null, tint = Color.Gray)
                     Spacer(modifier = Modifier.width(16.dp))
                     Column(modifier = Modifier.weight(1f)) {
                         Text("Emergency Override", color = Color.White)
                         Text("Restore ringer if 3+ calls from same person", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                     }
                     Switch(
                         checked = settings.allowOverride,
                         onCheckedChange = { viewModel.updateAllowOverride(it) }
                     )
                 }
                 Spacer(modifier = Modifier.height(48.dp))
                 
                 // START BUTTON
                 Button(
                     onClick = { viewModel.updateFocusMode(selectedMode) },
                     modifier = Modifier.fillMaxWidth().height(56.dp),
                     colors = ButtonDefaults.buttonColors(containerColor = activeColor)
                 ) {
                     Text("START ${selectedMode.uppercase()}", color = Color.Black, fontWeight = FontWeight.Bold)
                 }
                 
                 Spacer(modifier = Modifier.height(24.dp))
                 Divider(color = Color.DarkGray)
                 Spacer(modifier = Modifier.height(24.dp))
            } else {
                 // ACTIVE MODE UI
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = activeColor,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    "$selectedMode Mode Active",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "LifeOS is guarding your peace.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(48.dp))
            }

            // Alarm Section
            var wakeUpTime by remember { mutableStateOf(LocalTime.of(7, 0)) }
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Focus Alarm", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = wakeUpTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                        style = MaterialTheme.typography.displayLarge,
                        color = activeColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            TimePickerDialog(
                                context,
                                { _, h, m -> wakeUpTime = LocalTime.of(h, m) },
                                wakeUpTime.hour,
                                wakeUpTime.minute,
                                false
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333))
                    ) {
                        Text("Set Time")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val intent = android.content.Intent(android.provider.AlarmClock.ACTION_SET_ALARM).apply {
                                putExtra(android.provider.AlarmClock.EXTRA_HOUR, wakeUpTime.hour)
                                putExtra(android.provider.AlarmClock.EXTRA_MINUTES, wakeUpTime.minute)
                                putExtra(android.provider.AlarmClock.EXTRA_MESSAGE, "LifeOS Focus Wake Up")
                                putExtra(android.provider.AlarmClock.EXTRA_SKIP_UI, false)
                            }
                            try {
                                context.startActivity(intent)
                                Toast.makeText(context, "Opening Alarm App for $wakeUpTime", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Could not open Alarm App", Toast.LENGTH_SHORT).show()
                                e.printStackTrace()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = activeColor)
                    ) {
                        Icon(Icons.Filled.Alarm, null, modifier = Modifier.size(16.dp), tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Set System Alarm", color = Color.Black)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))

             // Auto Reply Settings
            if (!isFocusActive) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Message, null, tint = Color.Gray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Auto-Reply Messages", color = Color.White)
                            }
                            Switch(
                                checked = settings.isAutoReplyEnabled,
                                onCheckedChange = { viewModel.updateAutoReply(it) }
                            )
                        }
                        
                        if (settings.isAutoReplyEnabled) {
                            Divider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 12.dp))
                            
                            // Custom Reply Section
                            val currentCustomReply = when(selectedMode) {
                                "Sleep" -> settings.customReplySleep
                                "Driving" -> settings.customReplyDriving
                                "Meeting" -> settings.customReplyMeeting
                                else -> null
                            }
                            var showEditReplyDialog by remember { mutableStateOf(false) }
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically, 
                                modifier = Modifier.fillMaxWidth().padding(vertical=8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                 Column(modifier = Modifier.weight(1f)) {
                                     Text("Custom Reply Message", color = Color.White, style = MaterialTheme.typography.titleSmall)
                                     Text(
                                         if (!currentCustomReply.isNullOrBlank()) currentCustomReply else "Using Default", 
                                         color = Color.Gray, 
                                         style = MaterialTheme.typography.bodySmall, 
                                         maxLines = 1
                                     )
                                 }
                                 TextButton(onClick = { showEditReplyDialog = true }) {
                                     Text("Edit")
                                 }
                            }
                            
                            if (showEditReplyDialog) {
                                var tempReply by remember { mutableStateOf(currentCustomReply ?: "") }
                                AlertDialog(
                                    onDismissRequest = { showEditReplyDialog = false },
                                    title = { Text("Set Custom Reply for $selectedMode") },
                                    text = {
                                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                            OutlinedTextField(
                                                 value = tempReply,
                                                 onValueChange = { tempReply = it },
                                                 label = { Text("Message") },
                                                 placeholder = { Text("Leave empty to use default") }
                                            )
                                        }
                                    },
                                    confirmButton = {
                                        Button(onClick = {
                                             viewModel.updateFocusModeReply(selectedMode, if (tempReply.isBlank()) "" else tempReply) 
                                             showEditReplyDialog = false
                                        }) { Text("Save") }
                                    },
                                    dismissButton = { TextButton(onClick = { showEditReplyDialog = false }) { Text("Cancel") } }
                                )
                            }
                            
                            Divider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 12.dp))

                            // Whitelist management
                            var showAddContactDialog by remember { mutableStateOf(false) }
                            var newContactNumber by remember { mutableStateOf("") }
                            
                            Text("Allowed '$selectedMode' Contacts:", color = Color.White, style = MaterialTheme.typography.titleSmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            val allowedList = settings.sleepWhitelist.split(",").filter { it.isNotBlank() }
                            
                            if (allowedList.isEmpty()) {
                                Text("No numbers added yet.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                            }

                            allowedList.forEach { number ->
                                 Row(
                                     modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                     horizontalArrangement = Arrangement.SpaceBetween,
                                     verticalAlignment = Alignment.CenterVertically
                                 ) {
                                      Text(number, color = Color.LightGray, style = MaterialTheme.typography.bodyMedium)
                                      IconButton(onClick = { viewModel.removeSleepContact(number) }) {
                                          Icon(Icons.Filled.Close, "Remove", tint = Color.Red, modifier = Modifier.size(16.dp)) 
                                      }
                                 }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { showAddContactDialog = true }, modifier = Modifier.fillMaxWidth(), colors=ButtonDefaults.buttonColors(containerColor = Color(0xFF333333))) {
                                Text("Add Contact")
                            }
                            
                            if (showAddContactDialog) {
                                 AlertDialog(
                                     onDismissRequest = { showAddContactDialog = false },
                                     title = { Text("Add Contact Number") },
                                     text = {
                                         Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                             var contactName by remember { mutableStateOf<String?>(null) }
                                             if (contactName != null) {
                                                  Text("Contact: $contactName", color = activeColor, style = MaterialTheme.typography.labelMedium)
                                             }
                                             OutlinedTextField(
                                                 value = newContactNumber,
                                                 onValueChange = { 
                                                     newContactNumber = it
                                                     contactName = null
                                                 },
                                                 label = { Text("Phone Number") }
                                             )
                                             Spacer(modifier = Modifier.height(8.dp))
                                             Button(
                                                 onClick = { 
                                                     contactLauncher.launch(null) 
                                                 },
                                                 colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                             ) {
                                                 Text("Pick from Contacts")
                                             }
                                         }
                                     },
                                     confirmButton = {
                                         Button(onClick = {
                                             if (newContactNumber.isNotBlank()) {
                                                 viewModel.addSleepContact(newContactNumber)
                                                 newContactNumber = ""
                                                 showAddContactDialog = false
                                             }
                                         }) {
                                             Text("Add")
                                         }
                                     },
                                     dismissButton = {
                                         TextButton(onClick = { showAddContactDialog = false }) { Text("Cancel") }
                                     }
                                 )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Logic Summary:",
                                color = activeColor,
                                style = MaterialTheme.typography.labelSmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val activeMsg = if (!currentCustomReply.isNullOrBlank()) currentCustomReply else "I am $selectedMode - powered by LifeOS"
                            Text(
                                "• Added Above: \"$activeMsg\"",
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodySmall
                            )
                             Text(
                                "• Other Saved Contacts: \"I will call you later - powered by LifeOS\"",
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                "• Unknown: No reply",
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            } else {
                // If active, just show simplified status
            }
             
            Spacer(modifier = Modifier.height(32.dp))
            
            // Callback Log Section
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Auto-Reply Log", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (logs.isEmpty()) {
                        Text("No auto-replies sent yet.", color = Color.Gray)
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            logs.take(5).forEach { log ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(log.phoneNumber, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                                        Text(
                                            log.date.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")), 
                                            color = Color.Gray, 
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Text(
                                        if (log.messageSent.contains("powered")) "Sent" else "Busy",
                                        color = activeColor,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Divider(color = Color(0xFF333333))
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Turn Off Button
            if (isFocusActive) {
                Button(
                    onClick = {
                        viewModel.updateFocusMode("None")
                        // Optional: Navigate back or just refresh state to "Config"
                        // onExitFocusMode() // User can decide to stay and configure
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCF6679)),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Icon(Icons.Filled.PowerSettingsNew, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Turn Off Focus Mode")
                }
            } else {
                 Button(
                     onClick = onExitFocusMode,
                     colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
                     modifier = Modifier.fillMaxWidth()
                 ) {
                     Text("Back")
                 }
            }
        }
    }
}
