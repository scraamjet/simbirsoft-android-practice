package com.example.simbirsoft_android_practice.domain.usecase

import com.example.simbirsoft_android_practice.model.FilterCategory
import kotlinx.coroutines.flow.Flow

interface CategoriesFilterUseCase {
    suspend operator fun invoke(selectedIds: Flow<Set<Int>>): Flow<List<FilterCategory>>
}
