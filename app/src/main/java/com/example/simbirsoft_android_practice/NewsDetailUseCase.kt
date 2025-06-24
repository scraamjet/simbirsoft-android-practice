package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.model.NewsDetail

interface NewsDetailUseCase {
    suspend fun execute(newsId: Int): NewsDetail?
}