package com.example.core.service

import com.example.core.model.NewsItem
import kotlinx.coroutines.flow.Flow

interface NewsServiceGateway {
    fun getFilteredNews(selectedCategoryIds: Set<Int>): Flow<List<NewsItem>>
}