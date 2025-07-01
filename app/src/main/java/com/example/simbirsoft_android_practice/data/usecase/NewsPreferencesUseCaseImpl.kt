package com.example.simbirsoft_android_practice.data.usecase

import com.example.simbirsoft_android_practice.data.preferences.NewsPreferences
import com.example.core.usecase.NewsPreferencesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsPreferencesUseCaseImpl @Inject constructor(
    private val newsPreferences: NewsPreferences
) : NewsPreferencesUseCase {
    override fun getReadNewsIds(): Flow<Set<Int>> = newsPreferences.getReadNewsIds()
}
