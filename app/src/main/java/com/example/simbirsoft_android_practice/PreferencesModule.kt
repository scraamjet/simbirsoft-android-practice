package com.example.simbirsoft_android_practice

import android.app.Application
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.example.simbirsoft_android_practice.news.NewsPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule {

    @Provides
    @Singleton
    fun provideFilterPreferences(app: Application): FilterPreferences =
        FilterPreferences(app)

    @Provides
    @Singleton
    fun provideNewsPreferences(app: Application): NewsPreferences =
        NewsPreferences(app)
}
