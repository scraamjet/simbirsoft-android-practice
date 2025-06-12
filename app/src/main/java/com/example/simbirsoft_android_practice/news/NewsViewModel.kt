package com.example.simbirsoft_android_practice.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.example.simbirsoft_android_practice.model.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val filterPreferences: FilterPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        observeSelectedCategories()
    }

    private fun observeSelectedCategories() {
        viewModelScope.launch {
            filterPreferences.selectedCategories
                .distinctUntilChanged()
                .collectLatest { selectedCategories ->
                    loadNews(selectedCategories)
                }
        }
    }

    private suspend fun loadNews(selectedCategories: Set<Int>) {
        _uiState.value = NewsUiState.Loading

        eventRepository.getEvents(null)
            .flowOn(Dispatchers.IO)
            .catch { e ->
                _uiState.value = NewsUiState.Error(e.localizedMessage ?: "Unknown error")
            }
            .collect { events ->
                val filteredNews = events
                    .filter { event -> event.categoryIds.any { it in selectedCategories } }
                    .map(NewsMapper::eventToNewsItem)

                _uiState.value = if (filteredNews.isEmpty()) {
                    NewsUiState.NoResults
                } else {
                    NewsUiState.Results(filteredNews)
                }
            }
    }
}
