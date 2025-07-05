package com.example.simbirsoft_android_practice.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.EventServiceUseCase
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.example.simbirsoft_android_practice.model.Event
import com.example.simbirsoft_android_practice.model.NewsItem
import com.example.simbirsoft_android_practice.news.NewsMapper
import com.example.simbirsoft_android_practice.news.NewsPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val filterPreferences: FilterPreferences,
    private val eventServiceUseCase: EventServiceUseCase,
    private val newsPreferences: NewsPreferences,
) : ViewModel() {

    private val _bottomNavVisible = MutableStateFlow(true)
    val bottomNavigationVisible: StateFlow<Boolean> = _bottomNavVisible.asStateFlow()

    private val _readNewsIds = MutableStateFlow(newsPreferences.getReadNewsIds())

    private val _badgeFlow = MutableStateFlow(0)
    val badgeFlow: StateFlow<Int> = _badgeFlow.asStateFlow()

    init {
        observeNews()
    }

    private fun observeNews() {
        viewModelScope.launch {
            combine(
                eventServiceUseCase.events,
                filterPreferences.selectedCategories
            ) { eventList: List<Event>, selectedCategories: Set<Int> ->
                eventList
                    .filter { event ->
                        event.categoryIds.any { categoryId -> categoryId in selectedCategories }
                    }
                    .map { event -> NewsMapper.eventToNewsItem(event) }
            }.collect { filteredNewsItems: List<NewsItem> ->
                updateBadge(filteredNewsItems)
            }
        }
    }

    fun updateReadNews(newsId: Int) {
        _readNewsIds.update { currentSet ->
            if (newsId !in currentSet) {
                newsPreferences.markNewsAsReadAndSelected(newsId)
                currentSet + newsId
            } else {
                currentSet
            }
        }
    }

    fun updateBadge(newsItems: List<NewsItem>) {
        val unreadCount = newsItems.count { newsItem -> newsItem.id !in _readNewsIds.value }
        _badgeFlow.value = unreadCount
    }

    fun updateEventsFromService(eventList: List<Event>) {
        eventServiceUseCase.updateEvents(eventList)
    }

    fun setBottomNavigationVisible(visible: Boolean) {
        _bottomNavVisible.value = visible
    }
}




