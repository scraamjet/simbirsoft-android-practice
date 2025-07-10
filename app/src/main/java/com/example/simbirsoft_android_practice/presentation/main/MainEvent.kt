package com.example.simbirsoft_android_practice.presentation.main

import com.example.core.model.NewsItem
import com.example.simbirsoft_android_practice.domain.model.Event

sealed interface MainEvent {
    data class EventsFromServiceUpdated(val eventList: List<Event>) : MainEvent
    data class NewsUpdated(val newsItems: List<NewsItem>) : MainEvent
    data class NewsRead(val newsId: Int) : MainEvent
    data object InitReadNews : MainEvent
}