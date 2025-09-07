package com.example.news.domain.usecase

import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.core.notification.DonateWorkerKeys
import com.example.background.worker.DonateWorker
import javax.inject.Inject

class DonateUseCaseImpl @Inject constructor(
    private val workManager: WorkManager
) : DonateUseCase {

    override fun donate(newsId: Int, newsTitle: String, amount: Int) {
        val data = workDataOf(
            DonateWorkerKeys.NEWS_ID to newsId,
            DonateWorkerKeys.NEWS_TITLE to newsTitle,
            DonateWorkerKeys.AMOUNT to amount
        )

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        val request = OneTimeWorkRequestBuilder<DonateWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(request)
    }
}
