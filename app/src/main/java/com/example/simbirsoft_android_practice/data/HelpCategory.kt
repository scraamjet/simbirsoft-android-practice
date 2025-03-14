package com.example.simbirsoft_android_practice.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.simbirsoft_android_practice.R

enum class HelpCategory(
    @StringRes val titleResId: Int,
    @DrawableRes val icon: Int,
) {
    CHILDREN(R.string.help_children_category_title, R.drawable.help_children),
    ADULTS(R.string.help_adult_category_title, R.drawable.help_adults),
    ELDERLY(R.string.help_old_category_title, R.drawable.help_old),
    ANIMALS(R.string.help_animals_category_title, R.drawable.help_animals),
    EVENTS(R.string.help_events_category_title, R.drawable.help_events)
}
