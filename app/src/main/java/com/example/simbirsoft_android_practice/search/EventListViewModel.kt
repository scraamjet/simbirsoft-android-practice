package com.example.simbirsoft_android_practice.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.model.Event
import com.example.simbirsoft_android_practice.model.SearchEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TAG = "EventListViewModel"

@OptIn(ExperimentalCoroutinesApi::class)
class EventListViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")

    fun onSearchQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    val uiState: StateFlow<SearchUiState> =
        _query
            .flatMapLatest { searchQuery: String ->
                handleQuery(searchQuery)
            }
            .catch { exception: Throwable ->
                Log.e(TAG, "Search events loading exception", exception)
                emit(SearchUiState.Error(exception.localizedMessage ?: "Unknown error"))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = SearchUiState.Loading
            )

    private fun handleQuery(query: String): Flow<SearchUiState> {
        return eventRepository.getEvents(categoryId = null)
            .map { eventList: List<Event> ->
                eventList.map(SearchMapper::toSearchEvent)
            }
            .map { searchEvents: List<SearchEvent> ->
                val filtered = searchEvents.filter { searchEvent ->
                    searchEvent.title.contains(query, ignoreCase = true)
                }
                when {
                    query.isBlank() -> SearchUiState.BlankQuery
                    filtered.isEmpty() -> SearchUiState.Empty
                    else -> SearchUiState.Success(filtered)
                }
            }
    }
}

