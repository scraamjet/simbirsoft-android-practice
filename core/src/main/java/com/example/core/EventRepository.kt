package com.example.core

import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(categoryId: Int? = null): Flow<List<Event>>
}
