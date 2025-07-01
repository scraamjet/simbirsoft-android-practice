package com.example.core.usecase

import com.example.core.model.NewsItem
import kotlinx.coroutines.flow.StateFlow

interface NewsBadgeCountUseCase {
    fun observeBadgeCount(): StateFlow<Int>
    suspend fun updateNews(newsItems: List<NewsItem>)
}

