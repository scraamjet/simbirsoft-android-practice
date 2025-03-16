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
    val iconUrl: String? = null
) {
    fun toFilter(prefs: SharedPreferences): FilterCategory {
        return FilterCategory(
            id = id,
            title = title,
            iconUrl = iconUrl,
            isEnabled = prefs.getBoolean("category_$id", true),
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
