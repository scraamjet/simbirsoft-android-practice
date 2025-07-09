package com.example.simbirsoft_android_practice.search

import com.example.simbirsoft_android_practice.model.SearchEvent

sealed class SearchUiState {
    data object Loading : SearchUiState()
    data object BlankQuery : SearchUiState()
    data object Empty : SearchUiState()
    data class Success(val results: List<SearchEvent>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}
