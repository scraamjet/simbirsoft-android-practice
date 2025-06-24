package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.core.EventRepositoryImpl
import com.example.simbirsoft_android_practice.model.SearchEvent
import com.example.simbirsoft_android_practice.search.SearchMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventUseCaseImpl @Inject constructor(
    private val repository: EventRepository
) : EventsUseCase {
    override fun invoke(query: String): Flow<List<SearchEvent>> {
        return repository.getEvents(null)
            .map { events ->
                events.map(SearchMapper::toSearchEvent)
                    .filter { it.title.contains(query, ignoreCase = true) }
            }
    }
}
