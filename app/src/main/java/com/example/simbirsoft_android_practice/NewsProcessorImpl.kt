package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.domain.model.Event
import com.example.simbirsoft_android_practice.domain.model.NewsItem
import com.example.simbirsoft_android_practice.presentation.news.NewsMapper
import jakarta.inject.Inject

class NewsProcessorImpl @Inject constructor() : NewsProcessor {

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
