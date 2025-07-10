package com.example.simbirsoft_android_practice.presentation.main

import com.example.core.model.Event
import com.example.core.model.NewsItem

sealed interface MainEvent {
    data class EventsFromServiceUpdated(val eventList: List<Event>) : MainEvent
    data class NewsUpdated(val newsItems: List<NewsItem>) : MainEvent
    data class NewsRead(val newsId: Int) : MainEvent
    data object InitReadNews : MainEvent
}