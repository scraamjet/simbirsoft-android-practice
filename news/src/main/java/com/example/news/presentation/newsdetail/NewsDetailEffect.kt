package com.example.news.presentation.newsdetail

sealed class NewsDetailEffect {
    data object NavigateBack : NewsDetailEffect()
    data object ShowErrorToast : NewsDetailEffect()
    data class OpenHelpMoneyDialog(val newsId: Int, val newsTitle: String) : NewsDetailEffect()
}

