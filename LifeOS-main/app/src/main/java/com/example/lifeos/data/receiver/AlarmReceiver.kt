package com.example.lifeos.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.lifeos.data.manager.NotificationHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "LifeOS Reminder"
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: "You have a task due!"
        val id = intent.getIntExtra("EXTRA_ID", 0)

        val notificationHelper = NotificationHelper(context)
        
        // Check if notifications are enabled
        val app = context.applicationContext as com.example.lifeos.LifeOSApplication
        val repository = app.container.settingsRepository
        
        // We use goAsync for async work in Receiver
        val pendingResult = goAsync()
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            try {
                val settings = repository.settings.first()
                if (settings == null || settings.areNotificationsEnabled) {
                    notificationHelper.showNotification(id, title, message)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
