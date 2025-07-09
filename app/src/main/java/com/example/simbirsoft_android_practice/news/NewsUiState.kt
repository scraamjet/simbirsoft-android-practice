package com.example.simbirsoft_android_practice.news

import com.example.simbirsoft_android_practice.model.NewsItem

sealed class NewsUiState {
    data object Loading : NewsUiState()
    data class Results(val news: List<NewsItem>) : NewsUiState()
    data object NoResults : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}
