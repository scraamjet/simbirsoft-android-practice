package com.example.simbirsoft_android_practice.search

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object BlankQuery : SearchUiState
    data class QueryUpdated(val query: String) : SearchUiState
}
