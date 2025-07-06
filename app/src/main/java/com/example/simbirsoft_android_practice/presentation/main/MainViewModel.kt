package com.example.simbirsoft_android_practice.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.domain.usecase.EventServiceUseCase
import com.example.simbirsoft_android_practice.domain.usecase.ProcessNewsUseCase
import com.example.simbirsoft_android_practice.domain.model.Event
import com.example.simbirsoft_android_practice.domain.model.NewsItem
import com.example.simbirsoft_android_practice.domain.usecase.FilterPreferencesUseCase
import com.example.simbirsoft_android_practice.domain.usecase.NewsPreferencesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val filterPreferencesUseCase: FilterPreferencesUseCase,
    private val newsPreferencesUseCase: NewsPreferencesUseCase,
    private val eventServiceUseCase: EventServiceUseCase,
    private val processNewsUseCase: ProcessNewsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect: SharedFlow<MainEffect> = _effect.asSharedFlow()

    init {
        onEvent(MainEvent.InitReadNews)
        observeNews()
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.InitReadNews -> handleInitReadNews()
            is MainEvent.BottomNavVisibilityChanged -> handleBottomNavVisibilityChange(event.visible)
            is MainEvent.NewsRead -> handleNewsRead(event.newsId)
            is MainEvent.NewsUpdated -> handleNewsBadgeUpdated(event.newsItems)
            is MainEvent.RequestStartEventService -> handleRequestStartEventService()
        }
    }

    fun setBottomNavigationVisible(visible: Boolean) {
        onEvent(MainEvent.BottomNavVisibilityChanged(visible))
    }

    fun updateReadNews(newsId: Int) {
        onEvent(MainEvent.NewsRead(newsId))
    }

    fun updateBadgeCount(newsItems: List<NewsItem>) {
        onEvent(MainEvent.NewsUpdated(newsItems))
    }

    fun requestStartEventService() {
        onEvent(MainEvent.RequestStartEventService)
    }

    fun updateEventsFromService(eventList: List<Event>) {
        eventServiceUseCase.updateEvents(eventList)
    }

    private fun observeNews() {
        viewModelScope.launch {
            combine(
                eventServiceUseCase.events,
                filterPreferencesUseCase.getSelectedCategoryIds()
            ) { events: List<Event>, selectedCategoryIds: Set<Int> ->
                processNewsUseCase.filterAndMapEvents(events, selectedCategoryIds)
            }.collect { filteredNewsItems ->
                onEvent(MainEvent.NewsUpdated(filteredNewsItems))
            }
        }
    }

    private fun handleInitReadNews() {
        val readNewsIdsFromPrefs = newsPreferencesUseCase.getReadNewsIds()
        _state.update { previousState ->
            previousState.copy(readNewsIds = readNewsIdsFromPrefs)
        }
    }

    private fun handleBottomNavVisibilityChange(visible: Boolean) {
        _state.update { previousState ->
            previousState.copy(isBottomNavigationVisible = visible)
        }
    }

    private fun handleRequestStartEventService() {
        viewModelScope.launch {
            _effect.emit(MainEffect.StartAndBindEventService)
        }
    }

    private fun handleNewsRead(newsId: Int) {
        val currentReadNewsIds = _state.value.readNewsIds
        if (newsId !in currentReadNewsIds) {
            newsPreferencesUseCase.markNewsAsRead(newsId)
            _state.update { previousState ->
                val updatedReadIds = previousState.readNewsIds + newsId
                val updatedBadgeCount = previousState.badgeCount - 1
                previousState.copy(
                    readNewsIds = updatedReadIds,
                    badgeCount = updatedBadgeCount.coerceAtLeast(0),
                )
            }
        }
    }

    private fun handleNewsBadgeUpdated(newsItems: List<NewsItem>) {
        val readNewsIds = _state.value.readNewsIds
        val unreadCount = newsItems.count { newsItem -> newsItem.id !in readNewsIds }
        _state.update { previousState ->
            previousState.copy(badgeCount = unreadCount)
        }
    }
}


