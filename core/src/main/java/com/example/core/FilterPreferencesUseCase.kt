package com.example.core

import kotlinx.coroutines.flow.Flow

interface FilterPreferencesUseCase {
    fun getSelectedCategoryIds(): Flow<Set<Int>>
    suspend fun saveSelectedCategoryIds(ids: Set<Int>)
}