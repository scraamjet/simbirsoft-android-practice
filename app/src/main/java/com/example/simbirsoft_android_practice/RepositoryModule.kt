package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.domain.repository.CategoryRepository
import com.example.simbirsoft_android_practice.data.repository.CategoryRepositoryImpl
import com.example.simbirsoft_android_practice.domain.repository.EventRepository
import com.example.simbirsoft_android_practice.data.repository.EventRepositoryImpl
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.data.api.ApiService
import com.example.simbirsoft_android_practice.data.database.dao.CategoryDao
import com.example.simbirsoft_android_practice.data.database.dao.EventDao
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideEventRepository(
        extractor: JsonAssetExtractor,
        eventDao: EventDao,
        gson: Gson,
        apiService: ApiService,
    ): EventRepository = EventRepositoryImpl(extractor, eventDao, gson, apiService)

    @Provides
    @Singleton
    fun provideCategoryRepository(
        extractor: JsonAssetExtractor,
        categoryDao: CategoryDao,
        gson: Gson,
        apiService: ApiService,
    ): CategoryRepository = CategoryRepositoryImpl(extractor, categoryDao, gson, apiService)
}
