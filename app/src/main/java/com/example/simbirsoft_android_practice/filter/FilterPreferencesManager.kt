package com.example.simbirsoft_android_practice.filter

import android.content.Context

class FilterPreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences("filter_prefs", Context.MODE_PRIVATE)

    fun isCategorySelected(categoryId: Int): Boolean {
        return prefs.getStringSet("selected_categories", emptySet())
            ?.contains(categoryId.toString()) == true
    }

    fun getSelectedCategories(): List<Int> {
        return prefs.getStringSet("selected_categories", emptySet())
            ?.mapNotNull { it.toIntOrNull() } ?: emptyList()
    }

    fun saveSelectedCategories(categoryIds: List<Int>) {
        prefs.edit()
            .putStringSet("selected_categories", categoryIds.map { it.toString() }.toSet())
            .apply()
    }
}
