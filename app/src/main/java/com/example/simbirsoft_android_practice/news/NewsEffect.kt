package com.example.simbirsoft_android_practice.news

sealed class NewsEffect {
    data class NavigateToNewsDetail(val newsId: Int) : NewsEffect()
}