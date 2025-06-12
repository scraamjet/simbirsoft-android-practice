package com.example.simbirsoft_android_practice.search

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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventListViewModel @Inject constructor(
    private val eventRepository: EventRepository,
) : ViewModel() {

    private val _filteredEvents = MutableStateFlow<List<SearchEvent>>(emptyList())
    val filteredEvents: StateFlow<List<SearchEvent>> = _filteredEvents.asStateFlow()

    private val _uiState = MutableStateFlow(UiState.BlankQuery)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeSearchQuery(debouncedSearchFlow: Flow<String>) {
        viewModelScope.launch {
            debouncedSearchFlow
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        _uiState.value = UiState.BlankQuery
                        _filteredEvents.value = emptyList()
                        return@flatMapLatest emptyFlow()
                    }

                    _uiState.value = UiState.Loading

                    eventRepository.getEvents(null)
                        .map { list -> list.map(SearchMapper::toSearchEvent) }
                        .map { events ->
                            events.filter { it.title.contains(query, ignoreCase = true) }
                        }
                }
                .catch {
                    _uiState.value = UiState.Error
                    _filteredEvents.value = emptyList()
                }
                .collect { events ->
                    _filteredEvents.value = events
                    _uiState.value = when {
                        events.isEmpty() -> UiState.Empty
                        else -> UiState.Success
                    }
                }
        }
    }
}




