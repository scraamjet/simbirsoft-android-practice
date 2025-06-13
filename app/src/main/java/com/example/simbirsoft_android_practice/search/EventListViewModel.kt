package com.example.simbirsoft_android_practice.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.core.EventRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "EventListViewModel"

class EventListViewModel @Inject constructor(
    private val eventRepository: EventRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

        @OptIn(ExperimentalCoroutinesApi::class)
        fun observeSearchQuery(debouncedSearchFlow: Flow<String>) {
            viewModelScope.launch {
                debouncedSearchFlow
                    .flatMapLatest { query ->
                        eventRepository.getEvents(null)
                            .map { list -> list.map(SearchMapper::toSearchEvent) }
                            .map { events ->
                                events.filter { it.title.contains(query, ignoreCase = true) }
                            }
                            .map { filteredList -> Pair(filteredList, query) }
                    }
                    .catch { exception ->
                        _uiState.value =
                            SearchUiState.Error(exception.localizedMessage ?: "Unknown error")
                        Log.e(TAG, "Search events loading exception", exception)
                    }
                    .collect { (events, query) ->
                        _uiState.value =
                            when {
                                query.isBlank() -> SearchUiState.BlankQuery
                                events.isEmpty() -> SearchUiState.Empty
                                else -> SearchUiState.Success(events)
                            }
                    }
            }
        }
    }
