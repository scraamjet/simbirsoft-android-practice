package com.example.simbirsoft_android_practice.news

sealed class NewsDetailEffect {
    data object NavigateBack : NewsDetailEffect()
}