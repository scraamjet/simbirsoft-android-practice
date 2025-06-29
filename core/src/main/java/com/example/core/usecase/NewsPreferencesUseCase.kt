package com.example.core.usecase

interface NewsPreferencesUseCase {
    fun getReadNewsIds(): Set<Int>
    fun markNewsAsRead(newsId: Int)
}
