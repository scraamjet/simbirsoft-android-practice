package com.example.news.domain.usecase

import com.example.core.model.NewsItem
import com.example.core.repository.EventRepository
import com.example.core.mapper.NewsMapper
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