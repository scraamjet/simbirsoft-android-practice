package com.example.worker

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        val newsId = inputData.getInt("news_id", -1)
        val newsTitle = inputData.getString("news_title") ?: return Result.failure()
        val amount = inputData.getInt("amount", -1)
        if (newsId == -1 || amount <= 0) return Result.failure()

        val app = applicationContext.applicationContext as WorkerComponentProvider
        val notificationComponent = app.provideNotificationComponent() // ✅ создаем вручную

        val notificationManager = notificationComponent.notificationManager()

        val notification = NotificationCompat.Builder(applicationContext, "reminder_channel")
            .setSmallIcon(com.example.core.R.drawable.ic_ok)
            .setContentTitle("Напоминание о пожертвовании")
            .setContentText("Вы хотели пожертвовать $amount ₽ на: \"$newsTitle\"")
            .setAutoCancel(true)
            .build()

        notificationManager.notify(newsId, notification)

        return Result.success()
    }
}
