package com.example.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

class RemindLaterReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val newsId = intent.getIntExtra("news_id", -1)
        val newsTitle = intent.getStringExtra("news_title") ?: return
        val amount = intent.getIntExtra("amount", -1)
        if (newsId == -1 || amount <= 0) return

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(30, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresCharging(true)
                    .build()
            )
            .setInputData(
                workDataOf(
                    "news_id" to newsId,
                    "news_title" to newsTitle,
                    "amount" to amount
                )
            )
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}
