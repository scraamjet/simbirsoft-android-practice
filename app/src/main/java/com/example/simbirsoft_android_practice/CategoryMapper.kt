package com.example.simbirsoft_android_practice

import android.content.SharedPreferences
import com.example.simbirsoft_android_practice.data.HelpCategory

object CategoryMapper {
    fun toFilter(category: Category, prefs: SharedPreferences): FilterCategory {
        return FilterCategory(
            id = category.id,
            title = category.title,
            isEnabled = prefs.getStringSet("selected_categories", emptySet())
                ?.contains(category.id.toString()) == true
        )
    }

    fun toHelpCategory(category: Category): HelpCategory {
        return HelpCategory(
            id = category.id,
            title = category.title,
            iconUrl = category.iconUrl
        )
    }
}