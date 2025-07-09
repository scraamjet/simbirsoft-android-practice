package com.example.simbirsoft_android_practice.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val DEBOUNCE_DELAY_MILLISECONDS = 500L
private const val SEARCH_STATE_TIMEOUT_MILLISECONDS = 5000L

class SearchContainerViewModel @Inject constructor() : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(FlowPreview::class)
    val debouncedQuery: StateFlow<String> =
        searchQuery
            .debounce(DEBOUNCE_DELAY_MILLISECONDS)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = SEARCH_STATE_TIMEOUT_MILLISECONDS),
                initialValue = "",
            )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
