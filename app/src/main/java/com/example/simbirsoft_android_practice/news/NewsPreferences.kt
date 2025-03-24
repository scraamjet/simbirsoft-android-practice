package com.example.simbirsoft_android_practice.news

import android.content.Context
import androidx.core.content.edit

private const val PREFS_NAME = "news_prefs"
private const val KEY_SELECTED_NEWS_ID = "selected_news_id"
private const val NO_NEWS_SELECTED = -1

class NewsPreferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun saveSelectedNewsId(newsId: Int) {
        prefs.edit { putInt(KEY_SELECTED_NEWS_ID, newsId) }
    }

    fun getSelectedNewsId(): Int? {
        val selectedNewsId = prefs.getInt(KEY_SELECTED_NEWS_ID, NO_NEWS_SELECTED)
        return if (selectedNewsId == NO_NEWS_SELECTED) {
            null
        } else {
            selectedNewsId
        }
    }
}
