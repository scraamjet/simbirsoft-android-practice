package com.example.core.usecase

import kotlinx.coroutines.flow.Flow

interface FilterPreferencesUseCase {
    fun getSelectedCategoryIds(): Flow<Set<Int>>
    suspend fun saveSelectedCategoryIds(ids: Set<Int>)
}