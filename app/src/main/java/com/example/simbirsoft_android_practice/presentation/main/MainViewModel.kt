package com.example.simbirsoft_android_practice.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.model.NewsItem
import com.example.core.service.NewsServiceProxy
import com.example.core.usecase.FilterPreferencesUseCase
import com.example.core.usecase.NewsPreferencesUseCase
import com.example.core.usecase.StartNewsServiceUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val filterPreferencesUseCase: FilterPreferencesUseCase,
    private val newsPreferencesUseCase: NewsPreferencesUseCase,
    private val startNewsServiceUseCase: StartNewsServiceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect: SharedFlow<MainEffect> = _effect.asSharedFlow()

    private var serviceJob: Job? = null

    init {
        onEvent(MainEvent.InitReadNews)
        observeStartNewsRequests()
    }

    private fun observeStartNewsRequests() {
        viewModelScope.launch {
            startNewsServiceUseCase.observeRequests().collect {
                _effect.emit(MainEffect.StartAndBindNewsService)
            }
        }
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.InitReadNews -> handleInitReadNews()
            is MainEvent.BottomNavVisibilityChanged -> handleBottomNavVisibilityChange(event.visible)
            is MainEvent.NewsRead -> handleNewsRead(event.newsId)
            is MainEvent.NewsUpdated -> handleNewsBadgeUpdated(event.newsItems)
            is MainEvent.RequestStartNewsService -> handleRequestStartNewsService()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeNews(newsServiceProxy: NewsServiceProxy) {
        serviceJob?.cancel()
        serviceJob =
            viewModelScope.launch {
                filterPreferencesUseCase.getSelectedCategoryIds()
                    .distinctUntilChanged()
                    .flatMapLatest { selectedCategoryIds ->
                        newsServiceProxy.getFilteredNews(selectedCategoryIds)
                    }
                    .collect { filteredNewsItems ->
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

    private fun handleRequestStartNewsService() {
        viewModelScope.launch {
            _effect.emit(MainEffect.StartAndBindNewsService)
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
        val unreadCount =
            newsItems.count { newsItem ->
                newsItem.id !in readNewsIds
            }
        _state.update { previousState ->
            previousState.copy(badgeCount = unreadCount)
        }
    }
}
