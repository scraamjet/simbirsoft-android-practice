package com.example.simbirsoft_android_practice.presentation.main

sealed interface MainEffect {
    data object StartAndBindNewsService : MainEffect
}
