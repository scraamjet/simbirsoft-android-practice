package com.example.simbirsoft_android_practice.news

import android.content.Context

private const val PREFS_NAME = "news_prefs"
private const val KEY_SELECTED_NEWS_ID = "selected_news_id"
private const val DEFAULT_NEWS_ID = -1

class NewsPreferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSelectedNewsId(newsId: Int) {
        prefs.edit().putInt(KEY_SELECTED_NEWS_ID, newsId).apply()
    }

    fun getSelectedNewsId(): Int = prefs.getInt(KEY_SELECTED_NEWS_ID, DEFAULT_NEWS_ID)
}
