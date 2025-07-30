package com.example.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

class RemindLaterReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getIntExtra("news_id", -1)
        val eventName = intent.getStringExtra("news_title").orEmpty()
        val amount = intent.getIntExtra("amount", 0)

        if (eventId == -1 || eventName.isEmpty() || amount <= 0) return

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(eventId)

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "news_id" to eventId,
                    "news_title" to eventName,
                    "amount" to amount
                )
            )
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}

