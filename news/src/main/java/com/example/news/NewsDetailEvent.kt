package com.example.news

sealed class NewsDetailEvent {
    data class Load(val newsId: Int) : NewsDetailEvent()
}
