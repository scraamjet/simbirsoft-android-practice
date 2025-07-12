package com.example.simbirsoft_android_practice.domain.usecase

import com.example.simbirsoft_android_practice.domain.model.Event
import com.example.simbirsoft_android_practice.domain.model.NewsItem

interface ProcessNewsUseCase {
    fun filterAndMapEvents(
        eventList: List<Event>,
        selectedCategoryIds: Set<Int>
    ): List<NewsItem>
}
