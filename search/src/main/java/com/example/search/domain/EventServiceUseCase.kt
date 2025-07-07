package com.example.search.domain

import com.example.core.model.Event
import kotlinx.coroutines.flow.StateFlow

interface EventServiceUseCase {
    val events: StateFlow<List<Event>>
    fun updateEvents(events: List<Event>)
}

