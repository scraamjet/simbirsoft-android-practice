package com.example.simbirsoft_android_practice

interface NewsPreferencesUseCase {
    fun getReadNewsIds(): Set<Int>
    fun markNewsAsRead(newsId: Int)
}
