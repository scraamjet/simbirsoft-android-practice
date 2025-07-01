package com.example.simbirsoft_android_practice

import com.example.core.model.NewsItem
import com.example.core.usecase.NewsBadgeCountUseCase
import com.example.simbirsoft_android_practice.data.preferences.NewsPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsBadgeCountUseCaseImpl @Inject constructor(
    private val newsPreferences: NewsPreferences
) : NewsBadgeCountUseCase {

    private val badgeCount = MutableStateFlow(0)
    private val readNewsIds = MutableStateFlow<Set<Int>>(emptySet())

    init {
        observeReadNews()
    }

    private fun observeReadNews() {
        CoroutineScope(Dispatchers.IO).launch {
            newsPreferences.getReadNewsIds().collect { ids: Set<Int> ->
                readNewsIds.value = ids
            }
        }
    }

    override fun observeBadgeCount(): StateFlow<Int> = badgeCount

    override fun observeReadNewsIds(): StateFlow<Set<Int>> = readNewsIds

    override suspend fun updateNews(newsItems: List<NewsItem>) {
        val currentReadIds = readNewsIds.value
        val unreadCount = newsItems.count { newsItem -> newsItem.id !in currentReadIds }
        badgeCount.value = unreadCount
    }

    override suspend fun markNewsAsRead(newsId: Int) {
        newsPreferences.markNewsAsRead(newsId)
    }
}





