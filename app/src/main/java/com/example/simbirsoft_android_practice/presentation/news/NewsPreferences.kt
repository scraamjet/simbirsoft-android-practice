package com.example.simbirsoft_android_practice.presentation.news

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

private const val PREFS_NAME = "news_prefs"
private const val KEY_READ_NEWS_IDS = "read_news_ids"
private const val KEY_SELECTED_NEWS_ID = "selected_news_id"

class NewsPreferences @Inject constructor(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        fun markNewsAsReadAndSelected(newsId: Int) {
            val readNewsIds = getReadNewsIds().toMutableSet()
            readNewsIds.add(newsId)
            prefs.edit {
                putStringSet(KEY_READ_NEWS_IDS, readNewsIds.map { id -> id.toString() }.toSet())
                putString(KEY_SELECTED_NEWS_ID, newsId.toString())
            }
        }

        fun getReadNewsIds(): Set<Int> {
            val readNewsIdStrings = prefs.getStringSet(KEY_READ_NEWS_IDS, emptySet()) ?: emptySet()
            return readNewsIdStrings.mapNotNull { newsIdString -> newsIdString.toIntOrNull() }.toSet()
        }
    }
