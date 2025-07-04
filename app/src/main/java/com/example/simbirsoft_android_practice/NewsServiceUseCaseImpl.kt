package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.domain.model.Event
import com.example.simbirsoft_android_practice.domain.model.NewsItem
import com.example.simbirsoft_android_practice.presentation.news.NewsMapper
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class NewsServiceUseCaseImpl @Inject constructor() : NewsServiceUseCase {

    private val rawEvents: MutableStateFlow<List<Event>> = MutableStateFlow(emptyList())
    private val selectedCategoryIdsFlow: MutableStateFlow<Set<Int>> = MutableStateFlow(emptySet())

    private val _filteredNewsItems: StateFlow<List<NewsItem>> = combine(
        rawEvents,
        selectedCategoryIdsFlow
    ) { events: List<Event>, selectedCategoryIds: Set<Int> ->
        events
            .filter { event: Event ->
                event.categoryIds.any { categoryId: Int -> categoryId in selectedCategoryIds }
            }
            .map { event: Event -> NewsMapper.eventToNewsItem(event) }
    }.stateIn(
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    override val filteredNewsItems: StateFlow<List<NewsItem>> = _filteredNewsItems

    override fun updateRawNews(events: List<Event>) {
        rawEvents.value = events
    }

    override suspend fun applyCategoryFilter(selectedCategoryIds: Set<Int>) {
        selectedCategoryIdsFlow.emit(selectedCategoryIds)
    }
}




