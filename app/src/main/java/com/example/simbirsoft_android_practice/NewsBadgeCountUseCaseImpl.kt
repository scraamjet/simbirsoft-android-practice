package com.example.simbirsoft_android_practice

import com.example.core.model.NewsItem
import com.example.core.usecase.NewsBadgeCountUseCase
import com.example.simbirsoft_android_practice.data.preferences.NewsPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class NewsBadgeCountUseCaseImpl @Inject constructor(
    private val newsPreferences: NewsPreferences
) : NewsBadgeCountUseCase {

    private val badgeCount = MutableStateFlow(0)

    override fun observeBadgeCount(): StateFlow<Int> = badgeCount

    override suspend fun updateNews(newsItems: List<NewsItem>) {
        val readIds = newsPreferences.getReadNewsIds()
        val unreadCount = newsItems.count { newsItem -> newsItem.id !in readIds }
        badgeCount.value = unreadCount
    }
}


