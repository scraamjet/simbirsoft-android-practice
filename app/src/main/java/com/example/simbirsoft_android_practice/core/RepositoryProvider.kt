package com.example.simbirsoft_android_practice.core

import android.content.Context

interface RepositoryProvider {
    val newsRepository: NewsRepository
    val categoryRepository: CategoryRepository

    companion object {
        fun fromContext(context: Context): RepositoryProvider =
            (context.applicationContext) as? RepositoryProvider
                ?: error("RepositoryProvider was requested before application was ready")
    }
}
