package com.example.simbirsoft_android_practice.news

sealed class NewsEvent {
    data object LoadNews : NewsEvent()
    data class NewsClicked(val newsId: Int) : NewsEvent()
}