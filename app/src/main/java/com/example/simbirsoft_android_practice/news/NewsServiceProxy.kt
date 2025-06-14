package com.example.simbirsoft_android_practice.news

import com.example.simbirsoft_android_practice.model.NewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class NewsServiceProxy {
    private var newsService: NewsService? = null

    fun setService(service: NewsService) {
        this.newsService = service
    }

    fun clearService() {
        this.newsService = null
    }

    fun getFilteredNews(selectedCategoryIds: Set<Int>): Flow<List<NewsItem>> =
        flow {
            val flow = newsService?.loadAndFilterNews(selectedCategoryIds)
            if (flow != null) {
                emitAll(flow)
            } else {
                emit(emptyList())
            }
        }
}
