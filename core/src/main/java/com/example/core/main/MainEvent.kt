package com.example.core.main

import com.example.core.model.NewsItem

sealed interface MainEvent {
    data class NewsUpdated(val newsItems: List<NewsItem>) : MainEvent
    data class NewsRead(val newsId: Int) : MainEvent
    data class BottomNavVisibilityChanged(val visible: Boolean) : MainEvent
    data object InitReadNews : MainEvent
    data object RequestStartNewsService : MainEvent
}