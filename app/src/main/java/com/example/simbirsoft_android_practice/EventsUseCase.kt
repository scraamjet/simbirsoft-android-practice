package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.model.SearchEvent
import kotlinx.coroutines.flow.Flow

interface EventsUseCase {
    suspend operator fun invoke(query: String): Flow<List<SearchEvent>>
}
