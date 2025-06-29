package com.example.help

import com.example.core.model.HelpCategory
import com.example.core.repository.CategoryRepository
import com.example.core.mapper.CategoryMapper
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