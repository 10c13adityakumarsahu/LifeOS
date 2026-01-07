package com.example.lifeos.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.example.lifeos.ui.AppViewModelProvider

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onOpenVault: () -> Unit = {},
    onNavigateToFocus: () -> Unit = {}
) {
    val settingsState by viewModel.settings.collectAsState()
    val currentSettings = settingsState

    Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Settings", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        
        currentSettings?.let { settings ->
            // Profile
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Profile", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.material3.OutlinedTextField(
                        value = settings.userName,
                        onValueChange = { viewModel.updateUserName(it) },
                        label = { Text("Your Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (settings.userName.isNotEmpty()) {
                                IconButton(onClick = { viewModel.updateUserName("") }) {
                                    Icon(Icons.Filled.Close, "Clear")
                                }
                            }
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Security
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                     Text("Security & Vault", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                     Spacer(modifier = Modifier.height(8.dp))
                     
                     if (settings.pinCode == null) {
                         Text("To use the Secure Vault and App Lock, you must set a PIN first.", style = MaterialTheme.typography.bodyMedium)
                         Spacer(modifier = Modifier.height(8.dp))
                         androidx.compose.material3.Button(
                            onClick = { viewModel.setPin("0000") },
                            modifier = Modifier.fillMaxWidth()
                         ) {
                             Text("Setup PIN (Default 0000)")
                         }
                     } else {
                         androidx.compose.material3.Button(
                             onClick = onOpenVault, 
                             modifier = Modifier.fillMaxWidth(),
                             colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                         ) {
                             Text("Open Secure Vault")
                         }
                         
                         Spacer(modifier = Modifier.height(8.dp))
                         
                         androidx.compose.material3.OutlinedButton(
                            onClick = { viewModel.removePin() },
                            modifier = Modifier.fillMaxWidth()
                         ) {
                             Text("Remove PIN & Disable Vault")
                         }
                     }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // General
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(), 
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Allow Notifications", style = MaterialTheme.typography.titleMedium)
                    Switch(
                        checked = settings.areNotificationsEnabled,
                        onCheckedChange = { viewModel.updateNotificationsEnabled(it) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Focus Mode
             Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                         Text("Focus Mode", style = MaterialTheme.typography.titleMedium)
                         Text("Sleep, Driving, Meeting...", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    }
                    // If any mode is active, it shows checked. Or just "Configure >"
                    // Better to have a button "Configure" or "Start"
                    androidx.compose.material3.Button(onClick = onNavigateToFocus) {
                        Text(if (settings.currentFocusMode != "None") "Active: ${settings.currentFocusMode}" else "Start Focus")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Water Settings
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Water Reminders", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                        Switch(
                            checked = settings.isWaterReminderEnabled, 
                            onCheckedChange = { viewModel.updateWaterReminder(it) }
                        )
                    }
                    if (settings.isWaterReminderEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Interval: ${settings.waterReminderIntervalMinutes} mins")
                        Slider(
                            value = settings.waterReminderIntervalMinutes.toFloat(),
                            onValueChange = { viewModel.updateWaterInterval(it.toInt()) },
                            valueRange = 15f..120f,
                            steps = 6
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Sedentary Settings
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Sedentary Reminders", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                        Switch(
                            checked = settings.isSedentaryReminderEnabled, 
                            onCheckedChange = { viewModel.updateSedentaryReminder(it) }
                        )
                    }
                    if (settings.isSedentaryReminderEnabled) {
                         Spacer(modifier = Modifier.height(8.dp))
                         Text("Interval: ${settings.sedentaryReminderIntervalMinutes} mins")
                         Slider(
                            value = settings.sedentaryReminderIntervalMinutes.toFloat(),
                            onValueChange = { viewModel.updateSedentaryInterval(it.toInt()) },
                            valueRange = 30f..180f,
                            steps = 4
                        )
                    }
                }
            }
        }
    }
}
