package com.example.news

interface DonateUseCase {
    fun donate(newsId: Int, newsTitle: String, amount: Int)
}
