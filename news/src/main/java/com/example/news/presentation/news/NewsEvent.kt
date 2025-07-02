package com.example.news.presentation.news

sealed class NewsEvent {
    data object LoadNews : NewsEvent()
    data class NewsClicked(val newsId: Int) : NewsEvent()
    data object FiltersClicked : NewsEvent()
}
