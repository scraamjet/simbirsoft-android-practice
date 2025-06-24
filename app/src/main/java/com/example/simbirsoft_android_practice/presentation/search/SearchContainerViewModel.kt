package com.example.simbirsoft_android_practice.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEBOUNCE_DELAY_MILLISECONDS = 500L
private const val SEARCH_STATE_TIMEOUT_MILLISECONDS = 5000L

class SearchContainerViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<SearchContainerState>(SearchContainerState.Idle)
    val state: StateFlow<SearchContainerState> = _state.asStateFlow()

        private val _searchQuery = MutableStateFlow("")
        private val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

        private val _selectedTab = MutableStateFlow(SearchTab.EVENTS)
        private val selectedTab: StateFlow<SearchTab> = _selectedTab.asStateFlow()

        @OptIn(FlowPreview::class)
        val debouncedQuery: StateFlow<String> =
            searchQuery
                .debounce(DEBOUNCE_DELAY_MILLISECONDS)
                .distinctUntilChanged()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(SEARCH_STATE_TIMEOUT_MILLISECONDS),
                    initialValue = "",
                )

        init {
            observeCombinedState()
        }

        fun onEvent(event: SearchContainerEvent) {
            when (event) {
                is SearchContainerEvent.OnQueryChanged -> _searchQuery.value = event.query
                is SearchContainerEvent.OnPageChanged -> _selectedTab.value = SearchTab.fromPosition(event.position)
            }
        }

        private fun observeCombinedState() {
            viewModelScope.launch {
                combine(debouncedQuery, selectedTab) { query: String, tab: SearchTab ->
                    SearchContainerState.QueryAndPage(query = query, tab = tab)
                }.collect { combinedState: SearchContainerState ->
                    _state.value = combinedState
                }
            }
        }
    }
