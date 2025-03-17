package com.example.simbirsoft_android_practice.news

import android.content.Context

class NewsPreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences("news_prefs", Context.MODE_PRIVATE)

    fun saveSelectedNewsId(newsId: Int) {
        prefs.edit().putInt("selected_news_id", newsId).apply()
    }

    fun getSelectedNewsId(): Int {
        return prefs.getInt("selected_news_id", -1)
    }
}
