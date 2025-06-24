package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.model.HelpCategory
import kotlinx.coroutines.flow.Flow

interface CategoriesHelpUseCase {
    operator fun invoke(): Flow<List<HelpCategory>>
}