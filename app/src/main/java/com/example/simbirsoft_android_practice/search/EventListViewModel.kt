package com.example.simbirsoft_android_practice.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.model.SearchEvent
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

    private val _filteredEvents = MutableStateFlow<List<SearchEvent>>(emptyList())
    val filteredEvents: StateFlow<List<SearchEvent>> = _filteredEvents.asStateFlow()

    private val _uiState = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeSearchQuery(debouncedSearchFlow: Flow<String>) {
        viewModelScope.launch {
            debouncedSearchFlow
                .flatMapLatest { query ->
                    eventRepository.getEvents(null)
                        .map { list -> list.map(SearchMapper::toSearchEvent) }
                        .map { events ->
                            events.filter { event ->
                                event.title.contains(
                                    query,
                                    ignoreCase = true
                                )
                            }
                        }
                        .map { filteredList ->
                            Pair(filteredList, query)
                        }
                }
                .catch { exception ->
                    _uiState.value = UiState.Error
                    _filteredEvents.value = emptyList()
                    Log.e(
                        TAG,
                        "Search events loading exception: ${exception.localizedMessage}",
                        exception
                    )
                }
                .collect { (events, currentQuery) ->
                    _filteredEvents.value = events
                    _uiState.value = when {
                        currentQuery.isBlank() -> UiState.BlankQuery
                        events.isEmpty() -> UiState.Empty
                        else -> UiState.Success
                    }
                }
        }
    }
}



