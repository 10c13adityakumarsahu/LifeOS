package com.example.lifeos.ui.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.lifeos.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import com.example.lifeos.data.local.entity.MessageStatus

class MessageAlarmReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_SMS_SENT = "com.example.lifeos.SMS_SENT"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_SMS_SENT) {
            val id = intent.getIntExtra("ID", 0)
            val status = if (resultCode == android.app.Activity.RESULT_OK) MessageStatus.SENT else MessageStatus.FAILED
            
            updateMessageStatus(context, id, status)
            
            if (status == MessageStatus.SENT) {
                // Optional: Show notification only on success or failed?
                // For now, simple toast or log or existing notification logic?
                // Existing logic in sendSms showed notification immediately. 
                // We should move notification here?
                // Let's keep notification in 'sendSms' for "Attempting to send" or "Sent" (optimistic).
                // Or better: Show "Message Sent" here.
                showStatusNotification(context, id, "SMS Delivered", "Message sent successfully.")
            } else {
                showStatusNotification(context, id, "SMS Failed", "Failed to send message.")
            }
            return
        }

        val contactNumber = intent.getStringExtra("PHONE") ?: return
        val messageBody = intent.getStringExtra("MESSAGE") ?: return
        val id = intent.getIntExtra("ID", 0)
        val platformName = intent.getStringExtra("PLATFORM") ?: "WHATSAPP"
        
        if (platformName == "SMS") {
             sendSms(context, contactNumber, messageBody, id)
        } else {
             showSendNotification(context, id, contactNumber, messageBody)
        }
    }

    private fun updateMessageStatus(context: Context, id: Int, status: MessageStatus) {
         try {
            val app = context.applicationContext as com.example.lifeos.LifeOSApplication
            val repo = app.container.scheduledMessageRepository
            CoroutineScope(Dispatchers.IO).launch {
                val msgParams = repo.getMessageById(id)
                if (msgParams != null) {
                    val updated = msgParams.copy(status = status)
                    repo.updateMessage(updated)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendSms(context: Context, phone: String, message: String, id: Int) {
        val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(android.telephony.SmsManager::class.java)
        } else {
            android.telephony.SmsManager.getDefault()
        }
        
        try {
            // Auto-format for India if needed
            var finalPhone = phone.replace(" ", "").replace("-", "")
            if (!finalPhone.startsWith("+") && finalPhone.length == 10) {
                finalPhone = "+91$finalPhone"
            }
            
            val sentIntent = Intent(context, MessageAlarmReceiver::class.java).apply {
                action = ACTION_SMS_SENT
                putExtra("ID", id)
            }
            val sentPI = PendingIntent.getBroadcast(
                context, 
                id, 
                sentIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            smsManager.sendTextMessage(finalPhone, null, message, sentPI, null)
            
            // We don't update status here anymore, wait for callback.
            // But if we want PENDING -> SENT transition visuals immediately, maybe "SENDING"?
            // Keeping it PENDING until callback is fine.

             // Notification that we are TRYING to send? 
             // Or just wait for result? 
             // Let's show "Sending..."?
             // Previous code showed "SMS Sent".
             
        } catch (e: Exception) {
            e.printStackTrace()
            updateMessageStatus(context, id, MessageStatus.FAILED)
        }
    }
    
    private fun showStatusNotification(context: Context, id: Int, title: String, content: String) {
         val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("messages_channel", "Scheduled Messages", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
         val notification = NotificationCompat.Builder(context, "messages_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_email)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(id, notification)
    }

    private fun showSendNotification(context: Context, id: Int, phone: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "messages_channel",
                "Scheduled Messages",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        
        // Mark as SENT immediately for WhatsApp as we prompt user
        updateMessageStatus(context, id, MessageStatus.SENT)

        // WhatsApp Intent
        // ...
        val cleanPhone = phone.filter { it.isDigit() }
        val encodedMessage = java.net.URLEncoder.encode(message, "UTF-8")
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=$encodedMessage")
        
        val waIntent = Intent(Intent.ACTION_VIEW).apply {
            data = uri
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 
            id, 
            waIntent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "messages_channel")
            .setSmallIcon(android.R.drawable.ic_menu_send) // Use a valid icon resource
            .setContentTitle("Send WhatsApp Message")
            .setContentText("To $phone: $message")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Tap to send via WhatsApp:\n'$message'\nto $phone"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(id + 20000, notification)
    }
}
