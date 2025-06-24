package com.example.simbirsoft_android_practice.domain.repository

import com.example.simbirsoft_android_practice.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(categoryId: Int? = null): Flow<List<Event>>
}