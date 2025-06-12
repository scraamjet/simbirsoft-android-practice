package com.example.simbirsoft_android_practice.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.FlowPreview

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class SearchContainerViewModel @Inject constructor() : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(FlowPreview::class)
    val debouncedQuery: Flow<String> = searchQuery
        .debounce(500L)
        .distinctUntilChanged()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
