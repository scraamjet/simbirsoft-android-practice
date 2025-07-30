package com.example.worker

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DonateWorker(
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

        val remindIntent = PendingIntent.getBroadcast(
            applicationContext,
            newsId,
            Intent(applicationContext, RemindLaterReceiver::class.java).apply {
                putExtra("news_id", newsId)
                putExtra("news_title", newsTitle)
                putExtra("amount", amount)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, "donate_channel")
            .setSmallIcon(com.example.core.R.drawable.ic_ok)
            .setContentTitle("Пожертвование на сумму $amount ₽")
            .setContentText("Вы хотели помочь: \"$newsTitle\"")
            .setAutoCancel(true)
            .addAction(com.example.core.R.drawable.ic_ok, "Напомнить позже", remindIntent)
            .build()

        notificationManager.notify(newsId, notification)

        return Result.success()
    }
}



