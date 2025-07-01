package com.example.core.usecase

import com.example.core.model.NewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NewsBadgeCountUseCase {
    suspend fun markNewsAsRead(newsId: Int)
    suspend fun updateNews(newsItems: List<NewsItem>)
    fun observeBadgeCount(): StateFlow<Int>
    fun observeReadNewsIds(): Flow<Set<Int>>
}


