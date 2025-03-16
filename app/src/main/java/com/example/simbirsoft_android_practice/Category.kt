package com.example.simbirsoft_android_practice

import android.content.SharedPreferences
import com.example.simbirsoft_android_practice.data.HelpCategory
import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("icon_url")
    val iconUrl: String
) {
    fun toFilter(prefs: SharedPreferences): FilterCategory {
        return FilterCategory(
            id = id,
            title = title,
            isEnabled = prefs.getStringSet("selected_categories", emptySet())
                ?.contains(id.toString()) == true
        )
    }

    fun toHelpCategory(): HelpCategory {
        return HelpCategory(
            id = id,
            title = title,
            iconUrl = iconUrl
        )
    }
}

