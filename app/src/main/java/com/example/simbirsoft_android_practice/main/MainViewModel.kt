package com.example.simbirsoft_android_practice.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.example.simbirsoft_android_practice.model.Event
import com.example.simbirsoft_android_practice.model.NewsItem
import com.example.simbirsoft_android_practice.news.NewsMapper
import com.example.simbirsoft_android_practice.news.NewsPreferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

class MainViewModel @Inject constructor(
    filterPreferences: FilterPreferences,
    private val newsPreferences: NewsPreferences,
) : ViewModel() {

    private val _bottomNavVisible = MutableStateFlow(true)
    val bottomNavigationVisible: StateFlow<Boolean> = _bottomNavVisible.asStateFlow()

    private val _readNewsIds = MutableStateFlow(newsPreferences.getReadNewsIds())

    private val _badgeFlow = MutableStateFlow(0)
    val badgeFlow: StateFlow<Int> = _badgeFlow.asStateFlow()

    private val selectedCategories: Flow<Set<Int>> = filterPreferences.selectedCategories

    private var serviceJob: Job? = null

    fun observeNews(newsFlow: Flow<List<Event>>) {
        serviceJob?.cancel()
        serviceJob = viewModelScope.launch {
            selectedCategories
                .distinctUntilChanged()
                .collectLatest { selectedCategoriesSet ->
                    handleNewsFlow(newsFlow = newsFlow, selectedCategories = selectedCategoriesSet)
                }
        }
    }

    private suspend fun handleNewsFlow(
        newsFlow: Flow<List<Event>>,
        selectedCategories: Set<Int>
    ) {
        newsFlow
            .catch { exception ->
                Log.e(
                    TAG,
                    "News Service loading exception: ${exception.message}",
                    exception,
                )
            }
            .collect { eventList ->
                val filteredNewsItems: List<NewsItem> = eventList
                    .filter { event ->
                        event.categoryIds.any { categoryId -> categoryId in selectedCategories }
                    }
                    .map { event -> NewsMapper.eventToNewsItem(event) }

                updateBadge(newsItems = filteredNewsItems)
            }
    }

    fun updateReadNews(newsId: Int) {
        _readNewsIds.update { current ->
            if (newsId !in current) {
                val updated = current + newsId
                newsPreferences.markNewsAsReadAndSelected(newsId)
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
