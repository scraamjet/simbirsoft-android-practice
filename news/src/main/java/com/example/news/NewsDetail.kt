package com.example.news

data class NewsDetail(
    val title: String,
    val fullDescription: String,
    val startDateTime: Long,
    val endDateTime: Long,
    val owner: String,
    val ownerAddress: String,
    val ownerContacts: String,
    val picturesUrl: List<String>,
)
