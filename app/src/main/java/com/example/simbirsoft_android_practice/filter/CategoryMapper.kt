package com.example.simbirsoft_android_practice.filter

import com.example.simbirsoft_android_practice.model.Category
import com.example.simbirsoft_android_practice.model.FilterCategory
import com.example.simbirsoft_android_practice.model.HelpCategory

object CategoryMapper {
    fun toFilterCategory(
        category: Category,
        selectedIds: Set<Int>
    ): FilterCategory {
        return FilterCategory(
            id = category.id,
            title = category.name,
            isEnabled = category.id in selectedIds
        )
    }

    fun toHelpCategory(category: Category): HelpCategory {
        return HelpCategory(
            id = category.id,
            title = category.name,
            iconUrl = category.image,
        )
    }
}
