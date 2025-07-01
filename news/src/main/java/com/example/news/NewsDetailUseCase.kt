package com.example.news

interface NewsDetailUseCase {
    suspend fun execute(newsId: Int): NewsDetail?
}