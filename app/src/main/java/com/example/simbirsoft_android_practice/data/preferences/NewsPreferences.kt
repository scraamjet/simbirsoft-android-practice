package com.example.simbirsoft_android_practice.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PREFS_NAME = "news_prefs"
private val Context.newsDataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_NAME)

class NewsPreferences @Inject constructor(
    context: Context
) {
    private val dataStore = context.newsDataStore
    private val readIdsKey = stringSetPreferencesKey("read_news_ids")
    private val selectedIdKey = stringPreferencesKey("selected_news_id")

    fun getReadNewsIds(): Flow<Set<Int>> {
        return dataStore.data.map { preferences: Preferences ->
            preferences[readIdsKey]
                ?.mapNotNull { idString: String -> idString.toIntOrNull() }
                ?.toSet()
                ?: emptySet()
        }
    }

    suspend fun markNewsAsRead(newsId: Int) {
        dataStore.edit { preferences: MutablePreferences ->
            val currentSet = preferences[readIdsKey]
                ?.mapNotNull { idString: String -> idString.toIntOrNull() }
                ?.toMutableSet()
                ?: mutableSetOf()

            currentSet.add(newsId)
            preferences[readIdsKey] = currentSet.map { id: Int -> id.toString() }.toSet()
            preferences[selectedIdKey] = newsId.toString()
        }
    }
}

