package com.example.simbirsoft_android_practice.domain.usecase

import com.example.simbirsoft_android_practice.domain.model.SearchEvent
import kotlinx.coroutines.flow.Flow

interface EventListUseCase {
    suspend operator fun invoke(query: String): Flow<List<SearchEvent>>
}
