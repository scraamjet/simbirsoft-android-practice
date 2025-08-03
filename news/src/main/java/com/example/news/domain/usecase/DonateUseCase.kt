package com.example.news.domain.usecase

interface DonateUseCase {
    fun donate(newsId: Int, newsTitle: String, amount: Int)
}
