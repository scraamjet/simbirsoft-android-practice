package com.example.simbirsoft_android_practice.domain.usecase

interface NewsPreferencesUseCase {
    fun getReadNewsIds(): Set<Int>
    fun markNewsAsRead(newsId: Int)
}
