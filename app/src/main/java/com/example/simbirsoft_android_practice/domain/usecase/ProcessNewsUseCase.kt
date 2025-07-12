package com.example.simbirsoft_android_practice.domain.usecase

import com.example.core.model.NewsItem
import com.example.simbirsoft_android_practice.domain.model.Event

interface ProcessNewsUseCase {
    fun filterAndMapEvents(
        eventList: List<Event>,
        selectedCategoryIds: Set<Int>
    ): List<NewsItem>
}
