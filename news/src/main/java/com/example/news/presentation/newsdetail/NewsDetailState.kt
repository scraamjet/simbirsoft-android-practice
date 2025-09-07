package com.example.news.presentation.newsdetail

import com.example.core.model.NewsDetail

sealed class NewsDetailState {
    data object Idle : NewsDetailState()
    data class Result(val newsDetail: NewsDetail) : NewsDetailState()
    data object Error : NewsDetailState()
}


