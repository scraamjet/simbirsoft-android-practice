package com.example.simbirsoft_android_practice.presentation.search

sealed interface SearchContainerEvent {
    data class OnQueryChanged(val query: String) : SearchContainerEvent
}
