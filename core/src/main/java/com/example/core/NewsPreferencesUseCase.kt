package com.example.core

interface NewsPreferencesUseCase {
    fun getReadNewsIds(): Set<Int>
    fun markNewsAsRead(newsId: Int)
}
