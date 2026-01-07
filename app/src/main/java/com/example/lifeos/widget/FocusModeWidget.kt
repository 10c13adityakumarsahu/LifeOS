package com.example.lifeos.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.example.lifeos.R
import com.example.lifeos.data.local.LifeOSDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FocusModeWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateFocusWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        val action = intent.action
        val mode = when(action) {
            "ACTION_SLEEP" -> "Sleep"
            "ACTION_DRIVE" -> "Driving"
            "ACTION_MEET" -> "Meeting"
            "ACTION_OFF" -> "None"
            else -> null
        }

        if (mode != null) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = androidx.room.Room.databaseBuilder(
                        context.applicationContext,
                        LifeOSDatabase::class.java,
                        "lifeos_db"
                    ).build()
                    val settingsDao = db.settingsDao()
                    val currentSettings = settingsDao.getSettingsSnapshot()
                    
                    if (currentSettings != null) {
                        val isFocusActive = mode != "None"
                        val updated = currentSettings.copy(
                            currentFocusMode = mode,
                            isSleepModeEnabled = (mode == "Sleep"),
                            isWaterReminderEnabled = !isFocusActive,
                            isSedentaryReminderEnabled = !isFocusActive
                        )
                        settingsDao.updateSettings(updated)
                        
                        // Wait a bit or rely on onUpdate being called?
                        // Update UI manually for all widgets
                        val appWidgetManager = AppWidgetManager.getInstance(context)
                        val componentName = ComponentName(context, FocusModeWidget::class.java)
                        val ids = appWidgetManager.getAppWidgetIds(componentName)
                        for (id in ids) {
                           updateFocusWidget(context, appWidgetManager, id)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}

internal fun updateFocusWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val views = RemoteViews(context.packageName, R.layout.widget_focus_mode)

    // Set Listeners
    views.setOnClickPendingIntent(R.id.btn_sleep, getFocusPendingIntent(context, "ACTION_SLEEP"))
    views.setOnClickPendingIntent(R.id.btn_drive, getFocusPendingIntent(context, "ACTION_DRIVE"))
    views.setOnClickPendingIntent(R.id.btn_meet, getFocusPendingIntent(context, "ACTION_MEET"))
    views.setOnClickPendingIntent(R.id.btn_off, getFocusPendingIntent(context, "ACTION_OFF"))

    // Fetch State
    val scope = CoroutineScope(Dispatchers.IO)
    scope.launch {
         try {
             val db = androidx.room.Room.databaseBuilder(
                context.applicationContext,
                LifeOSDatabase::class.java,
                "lifeos_db"
            ).build()
            val settings = db.settingsDao().getSettingsSnapshot()
            val mode = settings?.currentFocusMode ?: "None"
            
            // Update UI based on mode
            views.setTextViewText(R.id.widget_status, "Current: $mode")
            
            if (mode == "None") {
                views.setViewVisibility(R.id.btn_off, View.GONE)
            } else {
                 views.setViewVisibility(R.id.btn_off, View.VISIBLE)
            }
            
            appWidgetManager.updateAppWidget(appWidgetId, views)
         } catch (e: Exception) {
             e.printStackTrace()
         }
    }
}

private fun getFocusPendingIntent(context: Context, action: String): PendingIntent {
    val intent = Intent(context, FocusModeWidget::class.java)
    intent.action = action
    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
}
