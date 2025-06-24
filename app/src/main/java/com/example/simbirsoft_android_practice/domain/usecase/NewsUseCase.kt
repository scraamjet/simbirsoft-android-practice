package com.example.simbirsoft_android_practice.domain.usecase

import com.example.simbirsoft_android_practice.model.NewsItem

interface NewsUseCase {
    suspend fun execute(selectedCategories: Set<Int>): List<NewsItem>
}