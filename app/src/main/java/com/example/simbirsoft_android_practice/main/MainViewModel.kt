package com.example.simbirsoft_android_practice.main

import androidx.lifecycle.ViewModel
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.example.simbirsoft_android_practice.model.NewsItem
import com.example.simbirsoft_android_practice.news.NewsPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MainViewModel @Inject constructor(
    filterPreferences: FilterPreferences,
    private val newsPreferences: NewsPreferences
) : ViewModel() {

    private val _bottomNavVisible = MutableStateFlow(true)
    val bottomNavigationVisible: StateFlow<Boolean> = _bottomNavVisible.asStateFlow()

    private val _readNewsIds =
        MutableStateFlow(newsPreferences.getReadNewsIds())
    val readNewsIds: StateFlow<Set<Int>> = _readNewsIds.asStateFlow()

    private val _badgeFlow = MutableStateFlow(0)
    val badgeFlow: StateFlow<Int> = _badgeFlow.asStateFlow()

    val selectedCategories: Flow<Set<Int>> = filterPreferences.selectedCategories

    fun updateReadNews(newsId: Int) {
        _readNewsIds.update { current ->
            if (newsId !in current) {
                val updated = current + newsId
                newsPreferences.markNewsAsReadAndSelected(newsId)
                updated
            } else current
        }
    }

    fun updateBadge(newsItems: List<NewsItem>) {
        val unreadCount = newsItems.count { newsItem -> newsItem.id !in _readNewsIds.value }
        _badgeFlow.value = unreadCount
    }

    fun showBottomNavigation() {
        _bottomNavVisible.value = true
    }

    fun hideBottomNavigation() {
        _bottomNavVisible.value = false
    }
}


