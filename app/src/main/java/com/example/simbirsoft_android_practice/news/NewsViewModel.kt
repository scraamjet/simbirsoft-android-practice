package com.example.simbirsoft_android_practice.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.example.simbirsoft_android_practice.model.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val filterPreferences: FilterPreferences
) : ViewModel() {

    private val _newsItems = MutableStateFlow<List<NewsItem>>(emptyList())
    val newsItems: StateFlow<List<NewsItem>> = _newsItems.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error.asSharedFlow()

    init {
        loadNews()
    }

    private fun loadNews() {
        viewModelScope.launch {
            _loading.value = true

            val selectedCategories = filterPreferences.getSelectedCategories()

            eventRepository.getEvents(null)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _error.emit(e.localizedMessage ?: "Unknown error")
                    _loading.value = false
                }
                .collect { events ->
                    val filteredNews = events
                        .filter { event -> event.categoryIds.any { it in selectedCategories } }
                        .map(NewsMapper::eventToNewsItem)

                    _newsItems.value = filteredNews
                    _loading.value = false
                }
        }
    }
}
