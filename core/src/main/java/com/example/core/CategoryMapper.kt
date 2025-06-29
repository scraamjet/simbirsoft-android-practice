package com.example.core


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
