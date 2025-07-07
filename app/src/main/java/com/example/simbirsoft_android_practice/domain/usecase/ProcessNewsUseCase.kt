package com.example.simbirsoft_android_practice.domain.usecase

import com.example.core.model.Event
import com.example.core.model.NewsItem

interface ProcessNewsUseCase {
    fun filterAndMapEvents(
        eventList: List<Event>,
        selectedCategoryIds: Set<Int>
    ): List<NewsItem>
}
