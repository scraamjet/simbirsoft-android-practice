package com.example.news.domain.usecase

import com.example.core.repository.EventRepository
import com.example.core.model.NewsDetail
import com.example.core.mapper.NewsMapper
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsDetailUseCaseImpl @Inject constructor(
    private val eventRepository: EventRepository
) : NewsDetailUseCase {
    override suspend fun execute(newsId: Int): NewsDetail? {
        return eventRepository.getEvents(null)
            .map { list ->
                list.find { event -> event.id == newsId }?.let(NewsMapper::eventToNewsDetail)
            }.firstOrNull()
    }
}
