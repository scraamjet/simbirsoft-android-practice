package com.example.simbirsoft_android_practice.search

sealed interface SearchContainerEvent {
    data class OnQueryChanged(val query: String) : SearchContainerEvent
}
