package com.example.news.presentation.newsdetail

sealed class NewsDetailEffect {
    data object NavigateBack : NewsDetailEffect()
    data object ShowErrorToast : NewsDetailEffect()
}

