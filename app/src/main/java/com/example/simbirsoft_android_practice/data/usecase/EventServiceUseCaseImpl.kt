package com.example.simbirsoft_android_practice.data.usecase

import com.example.core.model.Event
import com.example.search.domain.EventServiceUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EventServiceUseCaseImpl @Inject constructor() : EventServiceUseCase {

    private val _events: MutableStateFlow<List<Event>> = MutableStateFlow(emptyList())
    override val events: StateFlow<List<Event>> = _events.asStateFlow()

    override fun updateEvents(events: List<Event>) {
        _events.value = events

    }
}



