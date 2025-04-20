package com.example.simbirsoft_android_practice

import android.app.Application
import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.core.NewsRepository
import com.example.simbirsoft_android_practice.core.RepositoryProvider

class WantHelpApplication : Application(), RepositoryProvider {
    override val newsRepository: NewsRepository by lazy {
        NewsRepository(JsonAssetExtractor(this))
    }
    override val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(JsonAssetExtractor(this))
    }
}
