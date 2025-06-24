package com.example.simbirsoft_android_practice


import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.core.CategoryRepositoryImpl
import com.example.simbirsoft_android_practice.filter.CategoryMapper
import com.example.simbirsoft_android_practice.model.FilterCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CategoriesFilterUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : CategoriesFilterUseCase {
    override suspend fun invoke(selectedIds: Flow<Set<Int>>): Flow<List<FilterCategory>> {
        return combine(repository.getCategories(), selectedIds) { categories, ids ->
            categories.map { CategoryMapper.toFilterCategory(it, ids) }
        }
    }
}
