package com.example.news.domain.usecase

import com.example.core.model.NewsDetail

interface NewsDetailUseCase {
    suspend fun execute(newsId: Int): NewsDetail?
}