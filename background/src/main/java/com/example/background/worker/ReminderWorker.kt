package com.example.background.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.background.di.WorkerComponentProvider
import com.example.core.notification.DonateWorkerKeys
import com.example.core.notification.TypeNotification

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val newsId = inputData.getInt(DonateWorkerKeys.NEWS_ID, -1)
        val newsName = inputData.getString(DonateWorkerKeys.NEWS_TITLE).orEmpty()
        val amount = inputData.getInt(DonateWorkerKeys.AMOUNT, 0)

        if (newsId == -1 || newsName.isEmpty() || amount <= 0) {
            return Result.failure()
        }

        val notificationComponent = (applicationContext as WorkerComponentProvider)
            .provideAppNotifier()

        notificationComponent.makeStatusNotification(
            context = applicationContext,
            newsId = newsId,
            eventName = newsName,
            amount = amount,
            typeNotification = TypeNotification.REMINDER_NOTIFICATION
        )
        return Result.success()
    }
}
