package com.example.simbirsoft_android_practice

import android.app.Application
import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.core.RepositoryProvider

class App : Application(), RepositoryProvider {
    override val eventRepository: EventRepository by lazy {
        EventRepository(JsonAssetExtractor(this))
    }
    override val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(JsonAssetExtractor(this))
    }
}
