package com.example.simbirsoft_android_practice.data

data class NewsItem(
    val id: Int,
    val title: String,
    val description: String,
    val startDateTime: String,
    val endDateTime: String,
    val imageUrl: String
)
