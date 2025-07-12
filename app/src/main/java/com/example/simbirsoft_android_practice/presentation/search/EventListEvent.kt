package com.example.simbirsoft_android_practice.presentation.search

sealed class EventListEvent {
    data class SearchQueryChanged(val query: String) : EventListEvent()
}
