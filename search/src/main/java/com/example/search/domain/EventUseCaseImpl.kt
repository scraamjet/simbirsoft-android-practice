package com.example.search.domain

import com.example.core.mapper.SearchMapper
import com.example.core.model.SearchEvent
import com.example.core.repository.EventRepository
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
