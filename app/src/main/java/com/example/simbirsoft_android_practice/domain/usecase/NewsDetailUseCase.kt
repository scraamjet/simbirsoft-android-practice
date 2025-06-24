package com.example.simbirsoft_android_practice.domain.usecase

import com.example.simbirsoft_android_practice.model.NewsDetail

interface NewsDetailUseCase {
    suspend fun execute(newsId: Int): NewsDetail?
}