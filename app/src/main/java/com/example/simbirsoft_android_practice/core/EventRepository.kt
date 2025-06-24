package com.example.simbirsoft_android_practice.core

import com.example.simbirsoft_android_practice.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(categoryId: Int? = null): Flow<List<Event>>
}