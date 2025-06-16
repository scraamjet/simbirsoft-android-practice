package com.example.simbirsoft_android_practice.main

sealed interface MainEffect {
    data object StartAndBindNewsService : MainEffect
}
