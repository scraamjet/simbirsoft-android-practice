package com.example.simbirsoft_android_practice.data.usecase

import com.example.simbirsoft_android_practice.domain.usecase.FilterPreferencesUseCase
import com.example.simbirsoft_android_practice.presentation.filter.FilterPreferenceDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilterPreferencesUseCaseImpl @Inject constructor(
    private val dataStore: FilterPreferenceDataStore
) : FilterPreferencesUseCase {

    override fun getSelectedCategoryIds(): Flow<Set<Int>> {
        return dataStore.selectedCategories
    }

    override suspend fun saveSelectedCategoryIds(ids: Set<Int>) {
        dataStore.saveSelectedCategories(ids)
    }
}