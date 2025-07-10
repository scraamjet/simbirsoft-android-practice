package com.example.core.interactor

import com.example.core.model.NewsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface NewsBadgeCountInteractor {
    fun initializeBadgeObservers(scope: CoroutineScope)
    fun observeBadgeCount(): StateFlow<Int>
    fun observeReadNewsIds(): StateFlow<Set<Int>>
    suspend fun markNewsAsRead(newsId: Int)
    suspend fun updateNews(newsItems: List<NewsItem>)
}
