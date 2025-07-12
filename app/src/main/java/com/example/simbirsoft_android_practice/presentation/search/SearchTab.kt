package com.example.simbirsoft_android_practice.presentation.search

import androidx.annotation.StringRes
import com.example.simbirsoft_android_practice.R

enum class SearchTab(
    @StringRes val titleResId: Int,
) {
    EVENTS(R.string.tab_title_events),
    ORGANIZATIONS(R.string.tab_title_organizations);

    companion object {
        fun fromPosition(position: Int): SearchTab {
            return entries.getOrNull(position) ?: EVENTS
        }

        val count: Int get() = entries.size
    }
}
