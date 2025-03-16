package com.example.simbirsoft_android_practice

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("icon_url")
    val iconUrl: String? = null,
    var isEnabled: Boolean = true
)
