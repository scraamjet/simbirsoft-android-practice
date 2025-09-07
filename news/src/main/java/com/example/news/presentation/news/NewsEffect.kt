package com.example.news.presentation.news

sealed class NewsEffect {
    data class NavigateToNewsDetail(val newsId: Int) : NewsEffect()
    data object NavigateToFilter : NewsEffect()
    data object ShowErrorToast : NewsEffect()
}


