package com.example.simbirsoft_android_practice

data class FilterCategory(
    val id: Int,
    val title: String,
    val iconUrl: String? = null,
    var isEnabled: Boolean = true
)