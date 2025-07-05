package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.domain.model.Event
import com.example.simbirsoft_android_practice.domain.model.NewsItem
import com.example.simbirsoft_android_practice.presentation.news.NewsMapper
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class EventServiceUseCaseImpl @Inject constructor() : EventServiceUseCase {

    private val _events: MutableStateFlow<List<Event>> = MutableStateFlow(emptyList())
    override val events: StateFlow<List<Event>> = _events.asStateFlow()

    override fun updateEvents(events: List<Event>) {
        _events.value = events

    }
}



