package com.example.simbirsoft_android_practice.di

import com.example.simbirsoft_android_practice.core.utils.JsonAssetExtractor
import com.example.simbirsoft_android_practice.data.api.ApiService
import com.example.simbirsoft_android_practice.data.database.dao.CategoryDao
import com.example.simbirsoft_android_practice.data.database.dao.EventDao
import com.example.simbirsoft_android_practice.data.repository.CategoryRepositoryImpl
import com.example.simbirsoft_android_practice.data.repository.EventRepositoryImpl
import com.example.core.CategoryRepository
import com.example.core.EventRepository
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
