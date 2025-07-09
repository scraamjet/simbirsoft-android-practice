package com.example.simbirsoft_android_practice.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.NewsBadgeCountInteractor
import com.example.simbirsoft_android_practice.StartEventServiceUseCase
import com.example.simbirsoft_android_practice.domain.usecase.EventServiceUseCase
import com.example.simbirsoft_android_practice.domain.usecase.ProcessNewsUseCase
import com.example.simbirsoft_android_practice.domain.model.Event
import com.example.simbirsoft_android_practice.domain.model.NewsItem
import com.example.simbirsoft_android_practice.domain.usecase.FilterPreferencesUseCase
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
    private val newsBadgeCountInteractor: NewsBadgeCountInteractor,
    private val startEventServiceUseCase: StartEventServiceUseCase,
    private val eventServiceUseCase: EventServiceUseCase,
    private val processNewsUseCase: ProcessNewsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect: SharedFlow<MainEffect> = _effect.asSharedFlow()

    init {
        newsBadgeCountInteractor.initializeBadgeObservers(viewModelScope)
        observeStartEventRequests()
        observeNews()
        observeBadgeCount()
        onEvent(MainEvent.InitReadNews)
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.EventsFromServiceUpdated -> handleEventsFromServiceUpdated(event.eventList)
            is MainEvent.InitReadNews -> handleInitReadNews()
            is MainEvent.NewsRead -> handleNewsRead(event.newsId)
            is MainEvent.NewsUpdated -> handleNewsBadgeUpdated(event.newsItems)
        }
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

    private fun observeStartEventRequests() {
        viewModelScope.launch {
            startEventServiceUseCase.observeRequests().collect {
                _effect.emit(MainEffect.StartAndBindEventService)
            }
        }
    }

    private fun observeBadgeCount() {
        viewModelScope.launch {
            newsBadgeCountInteractor.observeBadgeCount().collect { count: Int ->
                _state.update { previousState ->
                    previousState.copy(badgeCount = count)
                }
            }
        }
    }

    private fun handleEventsFromServiceUpdated(events: List<Event>) {
        eventServiceUseCase.updateEvents(events)
    }

    private fun handleInitReadNews() {
        viewModelScope.launch {
            newsBadgeCountInteractor.observeReadNewsIds().collect { readIds: Set<Int> ->
                _state.update { previousState ->
                    previousState.copy(readNewsIds = readIds)
                }
            }
        }
    }

    private fun handleNewsRead(newsId: Int) {
        viewModelScope.launch {
            newsBadgeCountInteractor.markNewsAsRead(newsId = newsId)
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


