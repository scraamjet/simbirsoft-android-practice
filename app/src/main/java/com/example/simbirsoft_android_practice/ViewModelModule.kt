package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.example.simbirsoft_android_practice.main.MainViewModel
import com.example.simbirsoft_android_practice.news.NewsPreferences
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    fun provideMainViewModel(
        filterPreferences: FilterPreferences,
        newsPreferences: NewsPreferences,
    ): MainViewModel {
        return MainViewModel(filterPreferences, newsPreferences)
    }
}
