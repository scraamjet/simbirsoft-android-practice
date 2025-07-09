package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.domain.model.NewsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface NewsBadgeCountInteractor {
    fun initializeBadgeObservers(scope: CoroutineScope)
    fun observeBadgeCount(): StateFlow<Int>
    fun observeReadNewsIds(): StateFlow<Set<Int>>
    suspend fun markNewsAsRead(newsId: Int)
    suspend fun updateNews(newsItems: List<NewsItem>)
}
