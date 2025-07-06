package com.example.simbirsoft_android_practice.domain.usecase

import com.example.simbirsoft_android_practice.domain.model.Event
import kotlinx.coroutines.flow.StateFlow

interface EventServiceUseCase {
    val events: StateFlow<List<Event>>
    fun updateEvents(events: List<Event>)
}

