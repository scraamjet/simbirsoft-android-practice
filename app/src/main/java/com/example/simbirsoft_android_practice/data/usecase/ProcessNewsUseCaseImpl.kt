package com.example.simbirsoft_android_practice.data.usecase

import com.example.core.model.Event
import com.example.core.model.NewsItem
import com.example.simbirsoft_android_practice.domain.usecase.ProcessNewsUseCase
import com.example.simbirsoft_android_practice.presentation.news.NewsMapper
import jakarta.inject.Inject

class ProcessNewsUseCaseImpl @Inject constructor() : ProcessNewsUseCase {

    override fun filterAndMapEvents(
        eventList: List<Event>,
        selectedCategoryIds: Set<Int>
    ): List<NewsItem> {
        return eventList
            .filter { event: Event ->
                event.categoryIds.any { categoryId: Int -> categoryId in selectedCategoryIds }
            }
            .map { event: Event ->
                NewsMapper.eventToNewsItem(event)
            }
    }
}
