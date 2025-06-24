package com.example.simbirsoft_android_practice.presentation.news

sealed class NewsEffect {
    data class NavigateToNewsDetail(val newsId: Int) : NewsEffect()
}