package com.example.search

sealed class EventListEvent {
    data class SearchQueryChanged(val query: String) : EventListEvent()
}
