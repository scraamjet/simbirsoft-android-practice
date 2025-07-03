package com.example.simbirsoft_android_practice.domain

import com.example.simbirsoft_android_practice.data.preferences.FilterPreferences
import com.example.core.usecase.FilterPreferencesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilterPreferencesUseCaseImpl @Inject constructor(
    private val filterPreferences: FilterPreferences
) : FilterPreferencesUseCase {

    override fun getSelectedCategoryIds(): Flow<Set<Int>> {
        return filterPreferences.selectedCategories
    }

    override suspend fun saveSelectedCategoryIds(ids: Set<Int>) {
        filterPreferences.saveSelectedCategories(ids)
    }
}
