package com.example.simbirsoft_android_practice.filter

import android.content.Context

private const val PREFS_NAME = "filter_prefs"
private const val KEY_SELECTED_CATEGORIES = "selected_categories"

class FilterPreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isCategorySelected(categoryId: Int): Boolean {
        val selectedCategoryIds = getSelectedCategories()
        return selectedCategoryIds.contains(categoryId)
    }

    fun getSelectedCategories(): Set<Int> {
        val categoryIdStrings =
            prefs.getStringSet(KEY_SELECTED_CATEGORIES, emptySet()) ?: emptySet()

        return categoryIdStrings.mapNotNull { categoryIdString ->
            categoryIdString.toIntOrNull()
        }.toSet()
    }

    fun saveSelectedCategories(categoryIds: Set<Int>) {
        val categoryIdStrings = categoryIds.map { categoryId -> categoryId.toString() }.toSet()

        prefs.edit()
            .putStringSet(KEY_SELECTED_CATEGORIES, categoryIdStrings)
            .apply()
    }
}