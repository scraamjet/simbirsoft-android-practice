package com.example.search

import com.example.core.model.SearchEvent

sealed class EventListState {
    data object Loading : EventListState()
    data object Idle : EventListState()
    data object Empty : EventListState()
    data class Result(val results: List<SearchEvent>) : EventListState()
    data object Error : EventListState()
}
