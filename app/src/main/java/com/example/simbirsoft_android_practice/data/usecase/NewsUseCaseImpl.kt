package com.example.simbirsoft_android_practice.data.usecase

import com.example.simbirsoft_android_practice.domain.repository.EventRepository
import com.example.simbirsoft_android_practice.domain.usecase.NewsUseCase
import com.example.simbirsoft_android_practice.domain.model.NewsItem
import com.example.simbirsoft_android_practice.news.NewsMapper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsUseCaseImpl @Inject constructor(
    private val eventRepository: EventRepository
) : NewsUseCase {
    override suspend fun execute(selectedCategories: Set<Int>): List<NewsItem> {
        return eventRepository.getEvents(null)
            .map { events ->
                events.filter { event ->
                    event.categoryIds.any { categoryId -> categoryId in selectedCategories }
                }.map(NewsMapper::eventToNewsItem)
            }.first()
    }
}