package com.example.news

import com.example.core.model.NewsItem

sealed class NewsState {
    data object Loading : NewsState()
    data class Results(val newsList: List<NewsItem>) : NewsState()
    data object NoResults : NewsState()
    data object Error : NewsState()
}
