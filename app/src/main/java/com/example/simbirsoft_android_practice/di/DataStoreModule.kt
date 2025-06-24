package com.example.simbirsoft_android_practice.di

import android.content.Context
import com.example.simbirsoft_android_practice.presentation.filter.FilterPreferenceDataStore
import com.example.simbirsoft_android_practice.presentation.news.NewsPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataStoreModule {
    @Provides
    @Singleton
    fun provideFilterPreferenceDataStore(context: Context): FilterPreferenceDataStore = FilterPreferenceDataStore(context)

    @Provides
    @Singleton
    fun provideNewsPreferences(context: Context): NewsPreferences = NewsPreferences(context)
}
