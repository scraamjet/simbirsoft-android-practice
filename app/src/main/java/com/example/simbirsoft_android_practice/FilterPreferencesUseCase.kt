package com.example.simbirsoft_android_practice

import kotlinx.coroutines.flow.Flow

interface FilterPreferencesUseCase {
    fun getSelectedCategoryIds(): Flow<Set<Int>>
    suspend fun saveSelectedCategoryIds(ids: Set<Int>)
}