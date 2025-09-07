package com.example.core.mapper

import com.example.core.model.Category
import com.example.core.model.FilterCategory
import com.example.core.model.HelpCategory


object CategoryMapper {
    fun toFilterCategory(
        category: Category,
        selectedIds: Set<Int>,
    ): FilterCategory {
        return FilterCategory(
            id = category.id,
            title = category.name,
            isEnabled = category.id in selectedIds,
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
