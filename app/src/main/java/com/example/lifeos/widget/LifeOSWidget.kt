package com.example.lifeos.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.example.lifeos.LifeOSApplication
import com.example.lifeos.R
import com.example.lifeos.data.local.entity.FitnessLogEntity
import com.example.lifeos.data.local.entity.FitnessLogType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class LifeOSWidget : AppWidgetProvider() {

    companion object {
        const val ACTION_WATER_PLUS = "com.example.lifeos.widget.WATER_PLUS"
        const val ACTION_WATER_MINUS = "com.example.lifeos.widget.WATER_MINUS"
        const val ACTION_FOCUS_SLEEP = "com.example.lifeos.widget.FOCUS_SLEEP"
        const val ACTION_FOCUS_DRIVE = "com.example.lifeos.widget.FOCUS_DRIVE"
        const val ACTION_FOCUS_MEET = "com.example.lifeos.widget.FOCUS_MEET"
        const val ACTION_FOCUS_OFF = "com.example.lifeos.widget.FOCUS_OFF"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // 1. IMMEDIATE UPDATE: Show "Loading..." or static UI to prevent "Problem loading widget"
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_consolidated)
            views.setTextViewText(R.id.widget_task1, "Loading data...")
            try {
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) { e.printStackTrace() }
        }

        // 2. ASYNC UPDATE: Fetch data
        val pendingResult = goAsync()
        val app = context.applicationContext as LifeOSApplication
        CoroutineScope(Dispatchers.IO).launch {
            try {
                for (appWidgetId in appWidgetIds) {
                    updateWidgetData(context, appWidgetManager, appWidgetId, app)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action ?: return
        
        if (action.startsWith("com.example.lifeos.widget")) {
            val pendingResult = goAsync()
            val scope = CoroutineScope(Dispatchers.IO)
            
            scope.launch {
                try {
                    val app = context.applicationContext as LifeOSApplication
                    when (action) {
                        ACTION_WATER_PLUS, ACTION_WATER_MINUS -> {
                            val amount = if (action == ACTION_WATER_PLUS) 250.0 else -250.0
                            app.container.fitnessRepository.logActivity(
                                FitnessLogEntity(
                                    type = FitnessLogType.WATER,
                                    timestamp = LocalDateTime.now(),
                                    value = amount
                                )
                            )
                        }
                        ACTION_FOCUS_SLEEP, ACTION_FOCUS_DRIVE, ACTION_FOCUS_MEET, ACTION_FOCUS_OFF -> {
                            val targetMode = when (action) {
                                ACTION_FOCUS_SLEEP -> "Sleep"
                                ACTION_FOCUS_DRIVE -> "Driving"
                                ACTION_FOCUS_MEET -> "Meeting"
                                else -> "None" // ACTION_FOCUS_OFF
                            }
                            
                            val settingsRepo = app.container.settingsRepository
                            val currentSettings = settingsRepo.getSettingsSnapshot()
                            
                            if (currentSettings != null) {
                                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as android.media.AudioManager
                                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                                
                                var updatedSettings = currentSettings.copy(
                                    currentFocusMode = targetMode,
                                    isSleepModeEnabled = (targetMode == "Sleep")
                                )

                                if (targetMode != "None") {
                                    // TURN ON FOCUS
                                    if (currentSettings.isSilentModeEnabledForFocus) {
                                        // 1. Save current ringer mode if not already saved
                                        if (currentSettings.preFocusRingerMode == -1) {
                                            updatedSettings = updatedSettings.copy(preFocusRingerMode = audioManager.ringerMode)
                                        }
                                        
                                        // 2. Apply Silent Mode
                                        if (notificationManager.isNotificationPolicyAccessGranted) {
                                            audioManager.ringerMode = android.media.AudioManager.RINGER_MODE_SILENT
                                        } else {
                                            // Fallback
                                            try { audioManager.ringerMode = android.media.AudioManager.RINGER_MODE_VIBRATE } catch(_: Exception) {}
                                        }
                                    }
                                } else {
                                    // TURN OFF FOCUS
                                    // 1. Restore Ringer Mode
                                    val savedMode = currentSettings.preFocusRingerMode
                                    if (savedMode != -1) {
                                        val restoreTarget = if (savedMode != android.media.AudioManager.RINGER_MODE_SILENT) {
                                            savedMode
                                        } else {
                                            android.media.AudioManager.RINGER_MODE_NORMAL
                                        }
                                        
                                        try {
                                            audioManager.ringerMode = restoreTarget
                                        } catch (e: Exception) {
                                            try { audioManager.ringerMode = android.media.AudioManager.RINGER_MODE_NORMAL } catch(_: Exception) {}
                                        }
                                        // 2. Reset saved mode
                                        updatedSettings = updatedSettings.copy(preFocusRingerMode = -1)
                                    }
                                    
                                    // 3. Clear DND if needed (NotificationManager)
                                    if (notificationManager.isNotificationPolicyAccessGranted) {
                                        notificationManager.setInterruptionFilter(android.app.NotificationManager.INTERRUPTION_FILTER_ALL)
                                    }
                                }
                                
                                // Save to DB
                                settingsRepo.updateSettings(updatedSettings)
                            }
                        }
                    }

                    // Update all widgets
                    val man = AppWidgetManager.getInstance(context)
                    val ids = man.getAppWidgetIds(ComponentName(context, LifeOSWidget::class.java))
                    for (id in ids) {
                        updateWidgetData(context, man, id, app)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }

    private suspend fun updateWidgetData(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, app: LifeOSApplication) {
        try {
            val views = RemoteViews(context.packageName, R.layout.widget_consolidated)

            // --- Pending Intents for Buttons ---
            views.setOnClickPendingIntent(R.id.btn_water_plus, getPendingIntent(context, ACTION_WATER_PLUS))
            views.setOnClickPendingIntent(R.id.btn_water_minus, getPendingIntent(context, ACTION_WATER_MINUS))
            views.setOnClickPendingIntent(R.id.btn_sleep, getPendingIntent(context, ACTION_FOCUS_SLEEP))
            views.setOnClickPendingIntent(R.id.btn_drive, getPendingIntent(context, ACTION_FOCUS_DRIVE))
            views.setOnClickPendingIntent(R.id.btn_meet, getPendingIntent(context, ACTION_FOCUS_MEET))
            views.setOnClickPendingIntent(R.id.btn_off, getPendingIntent(context, ACTION_FOCUS_OFF))

            // --- Open App Intent ---
            val appIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            val appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_root, appPendingIntent)

            // --- Fetch Data ---
            val settings = app.container.settingsRepository.getSettingsSnapshot()
            val mode = settings?.currentFocusMode ?: "None"
            
            // Water
            val waterLogs = app.container.fitnessRepository.getLogsForDaySnapshot(LocalDateTime.now())
            val waterTotal = waterLogs.filter { it.type == FitnessLogType.WATER }.sumOf { it.value }.toInt()

            // Tasks
            val tasks = try {
                 app.container.taskRepository.getAllActiveTasksSync().take(2)
            } catch (e: Exception) { emptyList() }

            // --- Update Views ---
            views.setTextViewText(R.id.widget_status, mode.uppercase())
            views.setViewVisibility(R.id.btn_off, if (mode == "None") View.GONE else View.VISIBLE)
            views.setTextViewText(R.id.widget_water_text, "Water: $waterTotal ml")

            if (tasks.isNotEmpty()) {
                views.setTextViewText(R.id.widget_task1, "• " + tasks[0].title)
                if (tasks.size > 1) {
                    views.setTextViewText(R.id.widget_task2, "• " + tasks[1].title)
                    views.setViewVisibility(R.id.widget_task2, View.VISIBLE)
                } else {
                    views.setViewVisibility(R.id.widget_task2, View.GONE)
                }
            } else {
                views.setTextViewText(R.id.widget_task1, "No upcoming tasks")
                views.setViewVisibility(R.id.widget_task2, View.GONE)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)

        } catch (e: Exception) {
            e.printStackTrace()
             // Last resort error view
            val errorView = RemoteViews(context.packageName, R.layout.widget_consolidated)
            errorView.setTextViewText(R.id.widget_task1, "Tap to open app")
            appWidgetManager.updateAppWidget(appWidgetId, errorView)
        }
    }

    private fun getPendingIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, LifeOSWidget::class.java).apply { this.action = action }
        return PendingIntent.getBroadcast(
            context, 
            action.hashCode(), 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
