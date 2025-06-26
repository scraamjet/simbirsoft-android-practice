package com.example.simbirsoft_android_practice.presentation.service

import com.example.simbirsoft_android_practice.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NewsServiceProxy {
    private var newsService: NewsService? = null

    fun setService(service: NewsService) {
        this.newsService = service
    }

    fun clearService() {
        this.newsService = null
    }

    fun getFilteredNews(selectedCategoryIds: Set<Int>): Flow<List<NewsItem>> {
        return newsService?.loadAndFilterNews(selectedCategoryIds)
            ?: flowOf(emptyList())
    }
}
