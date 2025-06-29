package com.example.simbirsoft_android_practice.data.usecase

import com.example.core.FilterCategory
import com.example.core.CategoryRepository
import com.example.simbirsoft_android_practice.domain.usecase.CategoriesFilterUseCase
import com.example.core.CategoryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CategoriesFilterUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : CategoriesFilterUseCase {
    override suspend fun invoke(selectedIds: Flow<Set<Int>>): Flow<List<FilterCategory>> {
        return combine(
            repository.getCategories(),
            selectedIds
        ) { categories, ids ->
            categories.map { category ->
                CategoryMapper.toFilterCategory(
                    category,
                    ids
                )
            }
        }
    }
}