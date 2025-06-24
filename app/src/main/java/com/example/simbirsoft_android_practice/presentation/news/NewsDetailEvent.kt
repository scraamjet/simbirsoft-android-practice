package com.example.simbirsoft_android_practice.presentation.news

sealed class NewsDetailEvent {
    data class LoadNewsDetail(val newsId: Int) : NewsDetailEvent()
}
