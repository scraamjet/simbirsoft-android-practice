package com.example.simbirsoft_android_practice

data class NewsDetail(
    val title: String,
    val fullDescription: String,
    val startDateTime: String,
    val endDateTime: String,
    val owner: String,
    val ownerAddress: String,
    val ownerContacts: String,
    val picturesUrl: List<String>
)