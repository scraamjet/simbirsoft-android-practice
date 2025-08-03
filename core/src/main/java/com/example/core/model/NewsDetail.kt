package com.example.core.model

data class NewsDetail(
    val id: Int,
    val title: String,
    val fullDescription: String,
    val startDateTime: Long,
    val endDateTime: Long,
    val owner: String,
    val ownerAddress: String,
    val ownerContacts: String,
    val picturesUrl: List<String>,
)
