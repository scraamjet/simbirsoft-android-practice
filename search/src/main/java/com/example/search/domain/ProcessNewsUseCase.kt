package com.example.search.domain

import com.example.core.model.Event
import com.example.core.model.NewsItem

interface ProcessNewsUseCase {
    fun filterAndMapEvents(
        eventList: List<Event>,
        selectedCategoryIds: Set<Int>
    ): List<NewsItem>
}
