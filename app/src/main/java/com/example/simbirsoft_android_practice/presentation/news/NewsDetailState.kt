package com.example.simbirsoft_android_practice.presentation.news

import com.example.simbirsoft_android_practice.domain.model.NewsDetail

sealed class NewsDetailState {
    data class Result(val newsDetail: NewsDetail) : NewsDetailState()
    data class Error(val message: String) : NewsDetailState()
}

