package com.example.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.core.TypeNotification

class DonateWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val eventId = inputData.getInt("news_id", -1)
        val eventName = inputData.getString("news_title").orEmpty()
        val amount = inputData.getInt("amount", 0)

        if (eventId == -1 || eventName.isEmpty() || amount <= 0) {
            return Result.failure()
        }

        val notificationComponent = (applicationContext as WorkerComponentProvider)
            .provideNotificationComponent()

        notificationComponent.makeStatusNotification(
            context = applicationContext,
            eventId = eventId,
            eventName = eventName,
            amount = amount,
            typeNotification = TypeNotification.SEND_NOTIFICATION
        )

        return Result.success()
    }
}




