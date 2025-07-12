package com.example.simbirsoft_android_practice.presentation.news

sealed class NewsDetailEffect {
    data object NavigateBack : NewsDetailEffect()
    data object ShowErrorToast : NewsDetailEffect()
}

