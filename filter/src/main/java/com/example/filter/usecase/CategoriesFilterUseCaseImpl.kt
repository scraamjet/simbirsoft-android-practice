package com.example.filter.usecase

import com.example.core.model.FilterCategory
import com.example.core.repository.CategoryRepository
import com.example.core.mapper.CategoryMapper
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