package com.example.news

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.core.DonateWorkerKeys
import com.example.worker.DonateWorker
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

        val request = OneTimeWorkRequestBuilder<DonateWorker>()
            .setInputData(data)
            .build()

        workManager.enqueue(request)
    }
}
