package com.example.simbirsoft_android_practice.domain.usecase

import kotlinx.coroutines.flow.Flow

interface StartEventServiceUseCase {
    suspend fun requestStart()
    fun observeRequests(): Flow<Unit>
}
