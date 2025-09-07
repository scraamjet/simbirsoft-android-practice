package com.example.core.usecase

import kotlinx.coroutines.flow.Flow

interface StartEventServiceUseCase {
    suspend fun requestStart()
    fun observeRequests(): Flow<Unit>
}
