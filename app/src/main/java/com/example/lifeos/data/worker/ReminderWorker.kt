package com.example.lifeos.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.lifeos.data.manager.NotificationHelper

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val application = applicationContext as com.example.lifeos.LifeOSApplication
        val settingsRepository = application.container.settingsRepository
        val notificationHelper = NotificationHelper(applicationContext)

        // MVP: Simple heuristic check. In a real app, track last notification time.
        // For now, we just check if enabled.
        try {
            settingsRepository.settings.collect { settings ->
                if (settings != null) {
                    if (settings.isWaterReminderEnabled && !settings.isSleepModeEnabled) {
                        notificationHelper.showNotification(
                             1001, 
                             "Hydration Alert", 
                             "Time to drink water! Stay hydrated."
                        )
                    }
                    // Break after fetching once to avoid infinite collection
                    throw Exception("Done") 
                }
            }
        } catch (e: Exception) {
            // Flow collection terminated
        }

        return Result.success()
    }
}
