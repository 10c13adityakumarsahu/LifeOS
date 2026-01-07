package com.example.lifeos.ui.dashboard

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lifeos.ui.util.getPhoneNumberFromUri
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

import com.example.lifeos.data.local.entity.MessagePlatform
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleMessageDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, MessagePlatform) -> Unit
) {
    val context = LocalContext.current
    var phone by remember { mutableStateOf("") }
    var contactName by remember { mutableStateOf<String?>(null) }
    var countryCode by remember { mutableStateOf("+91") }
    var msg by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf(LocalTime.now().plusMinutes(15)) }
    var platform by remember { mutableStateOf(MessagePlatform.WHATSAPP) }
    
    val contactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri: android.net.Uri? ->
        uri?.let {
            val contactData = com.example.lifeos.ui.util.getContactDetailsFromUri(context, it)
            if (contactData != null) {
                phone = contactData.first
                contactName = contactData.second
            } else {
                Toast.makeText(context, "Failed to get contact info", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    val timePickerDialog = TimePickerDialog(
        context,
        { _, h, m -> selectedTime = LocalTime.of(h, m) },
        selectedTime.hour,
        selectedTime.minute,
        false
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule Message") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Platform Selection
                 Row(verticalAlignment = Alignment.CenterVertically) {
                     Text("Platform:")
                     Spacer(modifier = Modifier.width(8.dp))
                     FilterChip(selected = platform == MessagePlatform.WHATSAPP, onClick = { platform = MessagePlatform.WHATSAPP }, label = { Text("WhatsApp") })
                     Spacer(modifier = Modifier.width(8.dp))
                     FilterChip(selected = platform == MessagePlatform.SMS, onClick = { platform = MessagePlatform.SMS }, label = { Text("SMS") })
                 }

                if (contactName != null) {
                    Text(
                        text = "Contact: $contactName",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = countryCode,
                        onValueChange = { countryCode = it },
                        label = { Text("Code") },
                        modifier = Modifier.width(80.dp)
                    )
                    OutlinedTextField(
                        value = phone, 
                        onValueChange = { 
                            phone = it
                            contactName = null // Reset name if manually editing
                        }, 
                        label = { Text("Phone Number") },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { contactLauncher.launch(null) }) {
                        Icon(Icons.Filled.Contacts, "Contacts")
                    }
                }
                
                OutlinedTextField(
                    value = msg, 
                    onValueChange = { msg = it }, 
                    label = { Text("Message") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Time Picker
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth() 
                ) {
                    Text("Time:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { timePickerDialog.show() },
                        colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                         Icon(Icons.Filled.AccessTime, null, tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer)
                         Spacer(modifier = Modifier.width(8.dp))
                         Text(
                             selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a")), 
                             color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer
                         )
                    }
                }
                
                // Info text
                val now = LocalDateTime.now()
                var target = LocalDateTime.of(now.toLocalDate(), selectedTime)
                if (target.isBefore(now)) {
                    target = target.plusDays(1)
                }
                val diff = Duration.between(now, target).toMinutes()
                Text("Scheduled for: ${if (target.dayOfMonth != now.dayOfMonth) "Tomorrow" else "Today"}, in $diff mins", style = androidx.compose.material3.MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        },
        confirmButton = {
            Button(onClick = {
                val now = LocalDateTime.now()
                var target = LocalDateTime.of(now.toLocalDate(), selectedTime)
                if (target.isBefore(now)) {
                    target = target.plusDays(1)
                }
                val diffMinutes = Duration.between(now, target).toMinutes().toInt()
                
                if (diffMinutes > 0) {
                     onConfirm(phone, msg, diffMinutes, platform)
                     onDismiss()
                } else {
                     Toast.makeText(context, "Invalid time", Toast.LENGTH_SHORT).show()
                }
            }) { Text("Schedule") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
