package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.model.Event
import kotlinx.coroutines.flow.StateFlow

interface NewsServiceUseCase {
    val events: StateFlow<List<Event>>
    fun updateNews(events: List<Event>)
}
