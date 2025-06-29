package com.example.help

import com.example.core.HelpCategory
import com.example.core.CategoryRepository
import com.example.core.CategoryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoriesHelpUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : CategoriesHelpUseCase {
    override fun invoke(): Flow<List<HelpCategory>> =
        repository.getCategories().map { categories ->
            categories.map(CategoryMapper::toHelpCategory)
        }
}