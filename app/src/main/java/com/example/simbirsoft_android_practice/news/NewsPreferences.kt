package com.example.simbirsoft_android_practice.news

import android.content.Context
import androidx.core.content.edit

private const val PREFS_NAME = "news_prefs"
private const val KEY_SELECTED_NEWS_ID = "selected_news_id"

class NewsPreferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun saveSelectedNewsId(newsId: Int) {
        prefs.edit { putString(KEY_SELECTED_NEWS_ID, newsId.toString()) }
    }

    fun getSelectedNewsId(): Int? {
        val newsIdString = prefs.getString(KEY_SELECTED_NEWS_ID, null)
        return newsIdString?.toIntOrNull()
    }
}
