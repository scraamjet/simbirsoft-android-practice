package com.example.simbirsoft_android_practice.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsItem(
    val id: Int,
    val title: String,
    val description: String,
    val startDateTime: Long,
    val endDateTime: Long,
    val imageUrl: String,
) : Parcelable
