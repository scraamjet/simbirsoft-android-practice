package com.example.simbirsoft_android_practice.search

sealed class EventListEvent {
    data class SearchQueryChanged(val query: String) : EventListEvent()
}
