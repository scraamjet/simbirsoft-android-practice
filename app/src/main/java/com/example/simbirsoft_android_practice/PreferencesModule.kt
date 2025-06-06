package com.example.simbirsoft_android_practice

import android.content.Context
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.example.simbirsoft_android_practice.news.NewsPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule {
    @Provides
    @Singleton
    fun provideFilterPreferences(context: Context): FilterPreferences = FilterPreferences(context)
}
