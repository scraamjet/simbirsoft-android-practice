package com.example.simbirsoft_android_practice.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.EventsUseCase
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

class EventListViewModel @Inject constructor(
    private val eventsUseCase: EventsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeSearchQuery(debouncedSearchFlow: Flow<String>) {
        viewModelScope.launch {
            debouncedSearchFlow
                .flatMapLatest { query ->
                    eventsUseCase.invoke(query)
                        .map { events -> Pair(events, query) }
                }
                .catch { e -> _uiState.value = SearchUiState.Error(e.message ?: "Unknown error") }
                .collect { (events, query) ->
                    _uiState.value = when {
                        query.isBlank() -> SearchUiState.BlankQuery
                        events.isEmpty() -> SearchUiState.Empty
                        else -> SearchUiState.Success(events)
                    }
                }
        }
    }
}
