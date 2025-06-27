package com.example.simbirsoft_android_practice.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

private const val PREFS_NAME = "filter_prefs.preferences_pb"
private const val KEY_SELECTED_CATEGORIES = "selected_categories"

class FilterPreferences @Inject constructor(
    context: Context
) {

    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = {
            File(context.filesDir, PREFS_NAME)
        }
    )

        private val categoriesKey = stringSetPreferencesKey(KEY_SELECTED_CATEGORIES)

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
