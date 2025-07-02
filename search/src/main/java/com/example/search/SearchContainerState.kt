package com.example.search

sealed interface SearchContainerState {
    data object Idle : SearchContainerState
    data class QueryAndPage(val query: String, val tab: SearchTab) : SearchContainerState
}
