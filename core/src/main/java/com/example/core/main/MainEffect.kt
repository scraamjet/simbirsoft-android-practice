package com.example.core.main

sealed interface MainEffect {
    data object StartAndBindNewsService : MainEffect
}
