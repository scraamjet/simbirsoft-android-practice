package com.example.search.domain

import com.example.core.model.SearchEvent
import kotlinx.coroutines.flow.Flow

interface EventsUseCase {
    suspend operator fun invoke(query: String): Flow<List<SearchEvent>>
}
