package com.example.simbirsoft_android_practice.news

sealed class NewsDetailEvent {
    data class LoadNewsDetail(val newsId: Int) : NewsDetailEvent()
}
