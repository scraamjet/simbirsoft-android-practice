package com.example.simbirsoft_android_practice.presentation.news

import com.example.core.NewsItem

sealed class NewsState {
    data object Loading : NewsState()
    data class Results(val newsList: List<NewsItem>) : NewsState()
    data object NoResults : NewsState()
    data object Error : NewsState()
}
