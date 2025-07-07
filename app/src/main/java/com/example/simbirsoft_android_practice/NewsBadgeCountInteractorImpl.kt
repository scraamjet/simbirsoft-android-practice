package com.example.simbirsoft_android_practice

import com.example.core.model.NewsItem
import com.example.core.interactor.NewsBadgeCountInteractor
import com.example.simbirsoft_android_practice.data.preferences.NewsPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsBadgeCountInteractorImpl @Inject constructor(
    private val newsPreferences: NewsPreferences
) : NewsBadgeCountInteractor {

    private val _newsItems = MutableStateFlow<List<NewsItem>>(emptyList())
    private val _readNewsIds = MutableStateFlow<Set<Int>>(emptySet())
    private val _badgeCount = MutableStateFlow(0)

    override fun initializeBadgeObservers(scope: CoroutineScope) {
        scope.launch {
            newsPreferences.getReadNewsIds().collect { readNewsIdSet: Set<Int> ->
                _readNewsIds.update { readNewsIdSet }
            }
        }

        scope.launch {
            combine(_newsItems, _readNewsIds) { newsItemsList: List<NewsItem>, readIds: Set<Int> ->
                newsItemsList.count { newsItem: NewsItem -> newsItem.id !in readIds }
            }.collect { unreadNewsCount: Int ->
                _badgeCount.update { unreadNewsCount }
            }
        }
    }

    override fun observeBadgeCount(): StateFlow<Int> = _badgeCount

    override fun observeReadNewsIds(): StateFlow<Set<Int>> = _readNewsIds

    override suspend fun updateNews(newsItems: List<NewsItem>) {
        this._newsItems.update { newsItems }
    }

    override suspend fun markNewsAsRead(newsId: Int) {
        newsPreferences.markNewsAsRead(newsId)
    }
}





