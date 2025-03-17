package com.example.simbirsoft_android_practice.filter

data class FilterCategory(
    val id: Int,
    val title: String,
    var isEnabled: Boolean = true
)