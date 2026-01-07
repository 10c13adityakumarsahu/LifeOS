package com.example.lifeos.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.example.lifeos.R
import com.example.lifeos.data.local.entity.TaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class TasksWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateTasksWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

internal fun updateTasksWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val views = RemoteViews(context.packageName, R.layout.widget_tasks)
    
    val scope = CoroutineScope(Dispatchers.IO)
    scope.launch {
        try {
            val repository = (context.applicationContext as com.example.lifeos.LifeOSApplication).container.taskRepository

            // Fetch Tasks & Sort (Only include those with a start time for the widget display)
            val tasks = repository.getAllActiveTasksSync()
                .filter { it.startTime != null }
                .sortedBy { it.startTime }
                .take(5)
            
            val formatter = DateTimeFormatter.ofPattern("HH:mm")

            // Task 1
            if (tasks.isNotEmpty()) {
                val timeStr = tasks[0].startTime?.format(formatter) ?: "--:--"
                views.setTextViewText(R.id.widget_task1, "${tasks[0].title} ($timeStr)")
                views.setViewVisibility(R.id.widget_task1, View.VISIBLE)
            } else {
                 views.setTextViewText(R.id.widget_task1, "No upcoming tasks")
                 views.setViewVisibility(R.id.widget_task1, View.VISIBLE)
            }
            
            // Task 2
            if (tasks.size > 1) {
                val timeStr = tasks[1].startTime?.format(formatter) ?: "--:--"
                views.setTextViewText(R.id.widget_task2, "${tasks[1].title} ($timeStr)")
                views.setViewVisibility(R.id.widget_task2, View.VISIBLE)
            } else {
                views.setViewVisibility(R.id.widget_task2, View.GONE)
            }
            
            // Task 3
            if (tasks.size > 2) {
                val timeStr = tasks[2].startTime?.format(formatter) ?: "--:--"
                views.setTextViewText(R.id.widget_task3, "${tasks[2].title} ($timeStr)")
                views.setViewVisibility(R.id.widget_task3, View.VISIBLE)
            } else {
                views.setViewVisibility(R.id.widget_task3, View.GONE)
            }
            
            // Task 4
            if (tasks.size > 3) {
                val timeStr = tasks[3].startTime?.format(formatter) ?: "--:--"
                views.setTextViewText(R.id.widget_task4, "${tasks[3].title} ($timeStr)")
                views.setViewVisibility(R.id.widget_task4, View.VISIBLE)
            } else {
                views.setViewVisibility(R.id.widget_task4, View.GONE)
            }
            
            // Task 5
            if (tasks.size > 4) {
                val timeStr = tasks[4].startTime?.format(formatter) ?: "--:--"
                views.setTextViewText(R.id.widget_task5, "${tasks[4].title} ($timeStr)")
                views.setViewVisibility(R.id.widget_task5, View.VISIBLE)
            } else {
                views.setViewVisibility(R.id.widget_task5, View.GONE)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
