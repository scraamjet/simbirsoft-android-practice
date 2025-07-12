package com.example.simbirsoft_android_practice.data.usecase

import com.example.simbirsoft_android_practice.domain.model.HelpCategory
import com.example.simbirsoft_android_practice.domain.repository.CategoryRepository
import com.example.simbirsoft_android_practice.domain.usecase.CategoriesHelpUseCase
import com.example.simbirsoft_android_practice.presentation.filter.CategoryMapper
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