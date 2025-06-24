package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.core.CategoryRepositoryImpl
import com.example.simbirsoft_android_practice.filter.CategoryMapper
import com.example.simbirsoft_android_practice.model.HelpCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHelpCategoriesUseCaseImpl @Inject constructor(
    private val repository: CategoryRepositoryImpl
) : CategoriesHelpUseCase {
    override fun invoke(): Flow<List<HelpCategory>> =
        repository.getCategories().map { categories ->
            categories.map(CategoryMapper::toHelpCategory)
        }
}