package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.domain.model.Event
import com.example.simbirsoft_android_practice.domain.model.NewsItem
import kotlinx.coroutines.flow.StateFlow

interface NewsServiceUseCase {
    val filteredNewsItems: StateFlow<List<NewsItem>>
    fun updateRawNews(events: List<Event>)
    suspend fun applyCategoryFilter(selectedCategoryIds: Set<Int>)
}

