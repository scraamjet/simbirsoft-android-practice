package com.example.simbirsoft_android_practice.domain

import com.example.core.model.NewsItem
import com.example.core.interactor.NewsBadgeCountInteractor
import com.example.simbirsoft_android_practice.data.preferences.NewsPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsBadgeCountInteractorImpl @Inject constructor(
    private val newsPreferences: NewsPreferences
) : NewsBadgeCountInteractor {

    private val newsItems = MutableStateFlow<List<NewsItem>>(emptyList())
    private val readNewsIds = MutableStateFlow<Set<Int>>(emptySet())
    private val badgeCount = MutableStateFlow(0)

    override fun initializeBadgeObservers(scope: CoroutineScope) {
        scope.launch {
            newsPreferences.getReadNewsIds().collect { readNewsIdSet: Set<Int> ->
                readNewsIds.value = readNewsIdSet
            }
        }

        scope.launch {
            combine(newsItems, readNewsIds) { newsItems: List<NewsItem>, readIds: Set<Int> ->
                newsItems.count { newsItem: NewsItem -> newsItem.id !in readIds }
            }
                .collect { unreadNewsCount: Int ->
                    badgeCount.value = unreadNewsCount
                }
        }
    }

    override fun observeBadgeCount(): StateFlow<Int> = badgeCount

    override fun observeReadNewsIds(): StateFlow<Set<Int>> = readNewsIds

    override suspend fun updateNews(newsItems: List<NewsItem>) {
        this.newsItems.value = newsItems
    }

    override suspend fun markNewsAsRead(newsId: Int) {
        newsPreferences.markNewsAsRead(newsId)
    }
}




