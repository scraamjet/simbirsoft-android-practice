package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.model.SearchEvent
import kotlinx.coroutines.flow.Flow

interface EventsUseCase {
    fun invoke(query: String): Flow<List<SearchEvent>>
}
