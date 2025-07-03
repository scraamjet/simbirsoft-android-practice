package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.model.Event
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewsServiceUseCase @Inject constructor() {

    private val _newsFlow: MutableStateFlow<List<Event>> = MutableStateFlow(emptyList())
    private val newsFlow: StateFlow<List<Event>> = _newsFlow.asStateFlow()

    fun observeNewsFlow(): StateFlow<List<Event>> = newsFlow

    fun updateNews(eventList: List<Event>) {
        _newsFlow.value = eventList
    }
}



