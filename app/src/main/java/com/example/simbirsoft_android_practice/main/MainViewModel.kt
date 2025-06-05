package com.example.simbirsoft_android_practice.main

import androidx.lifecycle.ViewModel
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.example.simbirsoft_android_practice.news.NewsPreferences
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val filterPreferences: FilterPreferences,
    private val newsPreferences: NewsPreferences,
) : ViewModel() {

    fun getSelectedCategories(): Set<Int> = filterPreferences.getSelectedCategories()

    fun getReadNewsIds(): Set<Int> = newsPreferences.getReadNewsIds()
}
