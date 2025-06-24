package com.example.simbirsoft_android_practice.data.usecase

import com.example.simbirsoft_android_practice.domain.usecase.NewsPreferencesUseCase
import com.example.simbirsoft_android_practice.news.NewsPreferences
import javax.inject.Inject

class NewsPreferencesUseCaseImpl @Inject constructor(
    private val newsPreferences: NewsPreferences
) : NewsPreferencesUseCase {
    override fun getReadNewsIds(): Set<Int> = newsPreferences.getReadNewsIds()

    override fun markNewsAsRead(newsId: Int) {
        newsPreferences.markNewsAsReadAndSelected(newsId)
    }
}
