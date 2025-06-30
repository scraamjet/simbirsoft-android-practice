package com.example.core.usecase

import kotlinx.coroutines.flow.Flow

interface StartNewsServiceUseCase {
    suspend fun requestStart()
    fun observeRequests(): Flow<Unit>
}
