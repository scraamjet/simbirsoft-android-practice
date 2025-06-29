package com.example.simbirsoft_android_practice.data.usecase

import com.example.simbirsoft_android_practice.domain.model.SearchEvent
import com.example.core.repository.EventRepository
import com.example.simbirsoft_android_practice.domain.usecase.EventsUseCase
import com.example.simbirsoft_android_practice.presentation.search.SearchMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventUseCaseImpl @Inject constructor(
    private val repository: EventRepository
) : EventsUseCase {
    override suspend fun invoke(query: String): Flow<List<SearchEvent>> {
        return repository.getEvents(null)
            .map { events ->
                events.map(SearchMapper::toSearchEvent)
                    .filter { searchEvent ->
                        searchEvent.title.contains(
                            query,
                            ignoreCase = true
                        )
                    }
            }
    }
}
