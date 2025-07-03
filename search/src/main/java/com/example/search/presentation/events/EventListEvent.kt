package com.example.search.presentation.events

sealed class EventListEvent {
    data class SearchQueryChanged(val query: String) : EventListEvent()
}
