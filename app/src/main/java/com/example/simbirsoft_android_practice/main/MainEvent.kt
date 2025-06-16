package com.example.simbirsoft_android_practice.main

import com.example.simbirsoft_android_practice.model.NewsItem

sealed interface MainEvent {
    data class NewsUpdated(val newsItems: List<NewsItem>) : MainEvent
    data class NewsRead(val newsId: Int) : MainEvent
    data class BottomNavVisibilityChanged(val visible: Boolean) : MainEvent
    data object InitReadNews : MainEvent
    data object RequestStartNewsService : MainEvent
}