package com.example.simbirsoft_android_practice.di

import android.content.Context
import com.example.simbirsoft_android_practice.data.preferences.FilterPreferences
import com.example.simbirsoft_android_practice.data.preferences.NewsPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataStoreModule {
    @Provides
    @Singleton
    fun provideFilterPreferences(context: Context): FilterPreferences = FilterPreferences(context)

    @Provides
    @Singleton
    fun provideNewsPreferences(context: Context): NewsPreferences = NewsPreferences(context)
}
