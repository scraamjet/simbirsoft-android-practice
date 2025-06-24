package com.example.simbirsoft_android_practice.core

import com.example.simbirsoft_android_practice.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>
}