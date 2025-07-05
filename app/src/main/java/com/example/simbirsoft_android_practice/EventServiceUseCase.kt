package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.model.Event
import kotlinx.coroutines.flow.StateFlow

interface EventServiceUseCase {
    val events: StateFlow<List<Event>>
    fun updateEvents(events: List<Event>)
}
