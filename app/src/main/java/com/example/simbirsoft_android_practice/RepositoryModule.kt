package com.example.simbirsoft_android_practice

import android.app.Application
import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.database.CategoryDao
import com.example.simbirsoft_android_practice.database.EventDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideEventRepository(
        extractor: JsonAssetExtractor,
        eventDao: EventDao
    ): EventRepository = EventRepository(extractor, eventDao)

    @Provides
    @Singleton
    fun provideCategoryRepository(
        extractor: JsonAssetExtractor,
        categoryDao: CategoryDao
    ): CategoryRepository = CategoryRepository(extractor, categoryDao)

    @Provides
    fun provideJsonAssetExtractor(app: Application): JsonAssetExtractor =
        JsonAssetExtractor(app)
}
