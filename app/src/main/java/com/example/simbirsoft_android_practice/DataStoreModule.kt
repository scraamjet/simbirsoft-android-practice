package com.example.simbirsoft_android_practice

import android.content.Context
import com.example.simbirsoft_android_practice.filter.FilterPreferenceDataStore
import com.example.simbirsoft_android_practice.news.NewsPreferences
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
