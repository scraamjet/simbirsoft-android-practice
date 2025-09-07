package com.example.news.domain.usecase

import com.example.core.model.NewsItem

interface NewsUseCase {
    suspend fun execute(selectedCategories: Set<Int>): List<NewsItem>
}
