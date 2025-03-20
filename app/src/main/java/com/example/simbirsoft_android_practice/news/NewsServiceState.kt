package com.example.simbirsoft_android_practice.news

sealed class NewsServiceState {
    data object Disconnected : NewsServiceState()
    data object Connecting : NewsServiceState()
    data object Connected : NewsServiceState()
}