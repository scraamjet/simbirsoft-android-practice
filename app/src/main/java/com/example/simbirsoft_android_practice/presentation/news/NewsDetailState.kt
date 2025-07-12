package com.example.simbirsoft_android_practice.presentation.news

import com.example.simbirsoft_android_practice.domain.model.NewsDetail

sealed class NewsDetailState {
    data object Idle : NewsDetailState()
    data class Result(val newsDetail: NewsDetail) : NewsDetailState()
    data object Error : NewsDetailState()
}


