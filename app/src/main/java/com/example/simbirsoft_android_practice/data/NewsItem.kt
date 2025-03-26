package com.example.simbirsoft_android_practice.data

data class NewsItem(
    val id: Int,
    val title: String,
    val description: String,
    val startDateTime: Long,
    val endDateTime: Long,
    val imageUrl: String,
)
