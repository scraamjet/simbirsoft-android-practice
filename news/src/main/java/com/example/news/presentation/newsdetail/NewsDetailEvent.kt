package com.example.news.presentation.newsdetail

sealed class NewsDetailEvent {
    data class Load(val newsId: Int) : NewsDetailEvent()
}
