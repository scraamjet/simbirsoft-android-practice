package com.example.help

import com.example.core.model.HelpCategory
import kotlinx.coroutines.flow.Flow

interface CategoriesHelpUseCase {
    operator fun invoke(): Flow<List<HelpCategory>>
}
