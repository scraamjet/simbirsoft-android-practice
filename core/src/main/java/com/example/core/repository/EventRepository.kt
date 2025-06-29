package com.example.core.repository

import com.example.core.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(categoryId: Int? = null): Flow<List<Event>>
}
