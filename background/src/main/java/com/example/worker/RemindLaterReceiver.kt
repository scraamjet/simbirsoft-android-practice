package com.example.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.core.notification.DonateWorkerKeys
import java.util.concurrent.TimeUnit

class RemindLaterReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val newsId = intent.getIntExtra(DonateWorkerKeys.NEWS_ID, -1)
        val newsName = intent.getStringExtra(DonateWorkerKeys.NEWS_TITLE).orEmpty()
        val amount = intent.getIntExtra(DonateWorkerKeys.AMOUNT, 0)

        if (newsId == -1 || newsName.isEmpty() || amount <= 0) return

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(newsId)

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    DonateWorkerKeys.NEWS_ID to newsId,
                    DonateWorkerKeys.NEWS_TITLE to newsName,
                    DonateWorkerKeys.AMOUNT to amount
                )
            )
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}

