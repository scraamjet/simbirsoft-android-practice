package com.example.core

sealed interface MainEffect {
    data object StartAndBindNewsService : MainEffect
}
