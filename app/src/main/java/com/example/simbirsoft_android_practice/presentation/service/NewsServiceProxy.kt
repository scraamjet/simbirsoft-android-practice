package com.example.simbirsoft_android_practice.presentation.service

import com.example.core.NewsItem
import com.example.core.NewsServiceGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NewsServiceProxy: NewsServiceGateway {
    private var newsService: NewsService? = null

    fun setService(service: NewsService) {
        this.newsService = service
    }

    fun clearService() {
        this.newsService = null
    }

    override fun getFilteredNews(selectedCategoryIds: Set<Int>): Flow<List<NewsItem>> {
        return newsService?.loadAndFilterNews(selectedCategoryIds)
            ?: flowOf(emptyList())
    }
}
