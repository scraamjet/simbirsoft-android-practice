package com.example.simbirsoft_android_practice.presentation.search

import com.example.simbirsoft_android_practice.domain.model.SearchEvent

sealed class EventListState {
    data object Loading : EventListState()
    data object BlankQuery : EventListState()
    data object Empty : EventListState()
    data class Success(val results: List<SearchEvent>) : EventListState()
    data class Error(val message: String) : EventListState()
}
