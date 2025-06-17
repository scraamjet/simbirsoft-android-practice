package com.example.simbirsoft_android_practice.presentation.news

sealed class NewsDetailEvent {
    data class Load(val newsId: Int) : NewsDetailEvent()
}
