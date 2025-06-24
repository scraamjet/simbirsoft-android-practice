package com.example.simbirsoft_android_practice.news

import com.example.simbirsoft_android_practice.model.NewsItem

sealed class NewsState {
    data object Loading : NewsState()
    data class Results(val newsList: List<NewsItem>) : NewsState()
    data object NoResults : NewsState()
    data class Error(val message: String) : NewsState()
}
