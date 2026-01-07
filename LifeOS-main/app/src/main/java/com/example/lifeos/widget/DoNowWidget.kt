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
import kotlinx.coroutines.flow.first 
import java.time.LocalDate
import java.time.LocalDateTime

class DoNowWidget : AppWidgetProvider() {
    
    companion object {
        const val ACTION_WATER_PLUS = "com.example.lifeos.widget.WATER_PLUS"
        const val ACTION_WATER_MINUS = "com.example.lifeos.widget.WATER_MINUS"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action
        if (action == ACTION_WATER_PLUS || action == ACTION_WATER_MINUS) {
            val pendingResult = goAsync()
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                try {
                    val app = context.applicationContext as LifeOSApplication
                    val repo = app.container.fitnessRepository
                    
                    val amount = if (action == ACTION_WATER_PLUS) 250.0 else -250.0
                    
                    repo.logActivity(
                         FitnessLogEntity(
                             type = FitnessLogType.WATER,
                             timestamp = LocalDateTime.now(),
                             value = amount
                         )
                    )
                    
                    // Trigger update
                    val man = AppWidgetManager.getInstance(context)
                    val ids = man.getAppWidgetIds(ComponentName(context, DoNowWidget::class.java))
                    onUpdate(context, man, ids)
                    
                } catch(e: Exception) {
                    e.printStackTrace()
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val views = RemoteViews(context.packageName, R.layout.widget_donow)

    // Setup Intents
    val plusIntent = Intent(context, DoNowWidget::class.java).apply { action = DoNowWidget.ACTION_WATER_PLUS }
    val minusIntent = Intent(context, DoNowWidget::class.java).apply { action = DoNowWidget.ACTION_WATER_MINUS }
    
    views.setOnClickPendingIntent(R.id.btn_water_plus, PendingIntent.getBroadcast(context, 1, plusIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
    views.setOnClickPendingIntent(R.id.btn_water_minus, PendingIntent.getBroadcast(context, 2, minusIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))

    val scope = CoroutineScope(Dispatchers.IO)
    scope.launch {
        try {
            val app = context.applicationContext as LifeOSApplication
            val fitnessRepo = app.container.fitnessRepository
            val taskRepo = app.container.taskRepository
            
            val tasks = try {
                 taskRepo.activeTasks.first()
            } catch(e: Exception) { emptyList() }.take(3)
            
            // Fetch Water
            val waterLogs = fitnessRepo.getLogsForDaySnapshot(LocalDateTime.now())
            val waterTotal = waterLogs.filter { it.type == FitnessLogType.WATER }.sumOf { it.value }.toInt()

            val waterText = "Water: ${waterTotal}ml"
            views.setTextViewText(R.id.widget_water_text, waterText)
            
            // Tasks
            if (tasks.isNotEmpty()) {
                views.setTextViewText(R.id.widget_task1, tasks[0].title)
                views.setViewVisibility(R.id.widget_task1, View.VISIBLE)
            } else {
                 views.setTextViewText(R.id.widget_task1, "No upcoming tasks")
                 views.setViewVisibility(R.id.widget_task1, View.VISIBLE)
            }
            
            if (tasks.size > 1) {
                views.setTextViewText(R.id.widget_task2, tasks[1].title)
                views.setViewVisibility(R.id.widget_task2, View.VISIBLE)
            } else {
                views.setViewVisibility(R.id.widget_task2, View.GONE)
            }
            
             if (tasks.size > 2) {
                views.setTextViewText(R.id.widget_task3, tasks[2].title)
                views.setViewVisibility(R.id.widget_task3, View.VISIBLE)
            } else {
                views.setViewVisibility(R.id.widget_task3, View.GONE)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
