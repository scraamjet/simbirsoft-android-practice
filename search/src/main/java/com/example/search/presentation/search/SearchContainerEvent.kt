package com.example.search.presentation.search

sealed interface SearchContainerEvent {
    data class OnQueryChanged(val query: String) : SearchContainerEvent
    data class OnPageChanged(val position: Int) : SearchContainerEvent
}
