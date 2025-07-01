package com.example.news

sealed class NewsDetailState {
    data object Idle : NewsDetailState()
    data class Result(val newsDetail: NewsDetail) : NewsDetailState()
    data object Error : NewsDetailState()
}


