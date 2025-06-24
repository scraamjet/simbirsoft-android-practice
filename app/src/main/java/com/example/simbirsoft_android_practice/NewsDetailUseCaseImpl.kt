package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.core.EventRepositoryImpl
import com.example.simbirsoft_android_practice.model.NewsDetail
import com.example.simbirsoft_android_practice.news.NewsMapper
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsDetailUseCaseImpl @Inject constructor(
    private val eventRepository: EventRepositoryImpl
) : NewsDetailUseCase {
    override suspend fun execute(newsId: Int): NewsDetail? {
        return eventRepository.getEvents(null)
            .map { list ->
                list.find { event -> event.id == newsId }?.let(NewsMapper::eventToNewsDetail)
            }.firstOrNull()
    }
}
