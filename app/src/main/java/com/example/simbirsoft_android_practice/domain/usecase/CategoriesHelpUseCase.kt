package com.example.simbirsoft_android_practice.domain.usecase

import com.example.simbirsoft_android_practice.domain.model.HelpCategory
import kotlinx.coroutines.flow.Flow

interface CategoriesHelpUseCase {
    operator fun invoke(): Flow<List<HelpCategory>>
}