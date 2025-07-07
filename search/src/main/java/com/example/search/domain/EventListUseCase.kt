package com.example.search.domain

import com.example.core.model.SearchEvent
import kotlinx.coroutines.flow.Flow

interface EventListUseCase {
    suspend operator fun invoke(query: String): Flow<List<SearchEvent>>
}
