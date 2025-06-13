package com.example.simbirsoft_android_practice.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.filter.FilterPreferenceDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "NewsViewModel"

class NewsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val filterPreferenceDataStore: FilterPreferenceDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

        init {
            observeSelectedCategories()
        }

        private fun observeSelectedCategories() {
            viewModelScope.launch {
                filterPreferenceDataStore.selectedCategories
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
                .catch { exception ->
                    _uiState.value = NewsUiState.Error(exception.localizedMessage ?: "Unknown error")
                    Log.e(TAG, "News loading exception: ${exception.localizedMessage}", exception)
                }
                .collect { events ->
                    val filteredNews =
                        events
                            .filter { event -> event.categoryIds.any { categoryId -> categoryId in selectedCategories } }
                            .map(NewsMapper::eventToNewsItem)

                    _uiState.value =
                        if (filteredNews.isEmpty()) {
                            NewsUiState.NoResults
                        } else {
                            NewsUiState.Results(filteredNews)
                        }
                }
        }
    }
