package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.domain.repository.EventRepository
import com.example.simbirsoft_android_practice.domain.usecase.NewsDetailUseCase
import com.example.simbirsoft_android_practice.domain.model.NewsDetail
import com.example.simbirsoft_android_practice.news.NewsMapper
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
