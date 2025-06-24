package com.example.simbirsoft_android_practice.search

sealed class SearchContainerState {
    data object Idle : SearchContainerState()
    data object BlankQuery : SearchContainerState()
    data class QueryUpdated(val query: String) : SearchContainerState()
}
