package com.example.core.usecase

import kotlinx.coroutines.flow.Flow

interface NewsPreferencesUseCase {
    fun getReadNewsIds(): Flow<Set<Int>>
}
