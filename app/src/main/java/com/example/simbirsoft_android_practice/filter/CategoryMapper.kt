package com.example.simbirsoft_android_practice.filter

import com.example.simbirsoft_android_practice.data.Category
import com.example.simbirsoft_android_practice.data.FilterCategory
import com.example.simbirsoft_android_practice.data.HelpCategory

object CategoryMapper {
    fun toFilterCategory(
        category: Category,
        filterPrefs: FilterPreferencesManager
    ): FilterCategory {
        return FilterCategory(
            id = category.id,
            title = category.title,
            isEnabled = filterPrefs.isCategorySelected(category.id)
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
