package com.example.simbirsoft_android_practice.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.FilterPreferencesUseCase
import com.example.simbirsoft_android_practice.NewsPreferencesUseCase
import com.example.simbirsoft_android_practice.model.NewsItem
import com.example.simbirsoft_android_practice.news.NewsService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val filterPreferencesUseCase: FilterPreferencesUseCase,
    private val newsPreferencesUseCase: NewsPreferencesUseCase
) : ViewModel() {

    private val _bottomNavVisible = MutableStateFlow(true)
    val bottomNavigationVisible: StateFlow<Boolean> = _bottomNavVisible.asStateFlow()

    private val _readNewsIds = MutableStateFlow(newsPreferencesUseCase.getReadNewsIds())
    private val _badgeFlow = MutableStateFlow(0)
    val badgeFlow: StateFlow<Int> = _badgeFlow.asStateFlow()

    private var serviceJob: Job? = null

    fun startNewsUpdates(newsService: NewsService) {
        serviceJob?.cancel()
        serviceJob = viewModelScope.launch {
            filterPreferencesUseCase.getSelectedCategoryIds()
                .distinctUntilChanged()
                .collectLatest { selected ->
                    newsService.getFilteredNews(selected)
                        .collect { newsItems ->
                            updateBadge(newsItems)
                        }
                }
        }
    }

    fun updateReadNews(newsId: Int) {
        _readNewsIds.update { current ->
            if (newsId !in current) {
                val updated = current + newsId
                newsPreferencesUseCase.markNewsAsRead(newsId)
                updated
            } else {
                current
            }
        }
    }

    fun updateBadge(newsItems: List<NewsItem>) {
        val unreadCount = newsItems.count { newsItem -> newsItem.id !in _readNewsIds.value }
        _badgeFlow.value = unreadCount
    }

    fun setBottomNavigationVisible(visible: Boolean) {
        _bottomNavVisible.value = visible
    }
}

