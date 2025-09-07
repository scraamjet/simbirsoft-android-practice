package com.example.filter.usecase

import com.example.core.model.FilterCategory
import kotlinx.coroutines.flow.Flow

interface CategoriesFilterUseCase {
    suspend operator fun invoke(selectedIds: Flow<Set<Int>>): Flow<List<FilterCategory>>
}
