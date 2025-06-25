package com.example.simbirsoft_android_practice.filter

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PREFS_NAME = "filter_prefs"

private val Context.filterDataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFS_NAME
)

class FilterPreference @Inject constructor(
    context: Context
) {

    private val dataStore = context.filterDataStore

    private val categoriesKey = stringSetPreferencesKey("selected_categories")

    val selectedCategories: Flow<Set<Int>> =
        dataStore.data.map { preferences ->
            preferences[categoriesKey]
                ?.mapNotNull { category -> category.toIntOrNull() }
                ?.toSet()
                ?: emptySet()
        }

    suspend fun saveSelectedCategories(categories: Set<Int>) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[categoriesKey] =
                categories.map { category -> category.toString() }.toSet()
        }
    }
}

