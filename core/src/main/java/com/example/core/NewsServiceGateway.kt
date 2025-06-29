package com.example.core

import kotlinx.coroutines.flow.Flow

interface NewsServiceGateway {
    fun getFilteredNews(selectedCategoryIds: Set<Int>): Flow<List<NewsItem>>
}