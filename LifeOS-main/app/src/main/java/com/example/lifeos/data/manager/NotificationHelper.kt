package com.example.lifeos.data.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.lifeos.R

class NotificationHelper(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "LifeOS Reminders"
            val descriptionText = "Notifications for Tasks and Fitness"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("lifeos_channel_id", name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(id: Int, title: String, message: String) {
        // Create Intents for Actions
        
        // 1. DONE / COMPLETE Action
        // We need to distinguish between Hydration and Tasks. 
        // For now, if ID is generic hydration ID (e.g. 1001), use Hydration logic.
        // If ID is Task ID (which are usually small ints?), use Task logic.
        // Alternatively, use different Action strings.
        
        val isHydration = (id == 1001) // Simple heuristic based on ReminderWorker
        
        val doneIntent = android.content.Intent(context, com.example.lifeos.data.receiver.NotificationActionReceiver::class.java).apply {
            action = if (isHydration) com.example.lifeos.data.receiver.NotificationActionReceiver.ACTION_HYDRATION_DONE 
                     else com.example.lifeos.data.receiver.NotificationActionReceiver.ACTION_TASK_DONE
            putExtra(com.example.lifeos.data.receiver.NotificationActionReceiver.EXTRA_NOTIFICATION_ID, id)
             if (!isHydration) {
                putExtra(com.example.lifeos.data.receiver.NotificationActionReceiver.EXTRA_TASK_ID, id)
            }
        }
        val donePendingIntent = android.app.PendingIntent.getBroadcast(
            context, 
            id + 1000, 
            doneIntent, 
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        // 2. SNOOZE / REMIND AGAIN Action
        val snoozeIntent = android.content.Intent(context, com.example.lifeos.data.receiver.NotificationActionReceiver::class.java).apply {
            action = com.example.lifeos.data.receiver.NotificationActionReceiver.ACTION_SNOOZE
            putExtra(com.example.lifeos.data.receiver.NotificationActionReceiver.EXTRA_NOTIFICATION_ID, id)
            putExtra("EXTRA_TITLE", title)
            putExtra("EXTRA_MESSAGE", message)
        }
        val snoozePendingIntent = android.app.PendingIntent.getBroadcast(
            context,
            id + 2000,
            snoozeIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )
        
        // 3. Open App Intent (Clicking body)
        // If it's a task, maybe open that task? For now launch app.
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val contentPendingIntent = if (launchIntent != null) {
            android.app.PendingIntent.getActivity(
                context, 0, launchIntent, android.app.PendingIntent.FLAG_IMMUTABLE
            )
        } else null

        val builder = NotificationCompat.Builder(context, "lifeos_channel_id")
            .setSmallIcon(R.drawable.ic_launcher) // Assumes default icon exists
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(contentPendingIntent)
            .addAction(android.R.drawable.ic_menu_agenda, "Done", donePendingIntent)
            .addAction(android.R.drawable.ic_popup_reminder, "Remind Again", snoozePendingIntent)

        notificationManager.notify(id, builder.build())
    }
}
