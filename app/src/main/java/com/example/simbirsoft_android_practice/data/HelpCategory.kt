package com.example.simbirsoft_android_practice.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class HelpCategory(
    val id: Int,
    val title: String,
    val iconUrl: String,
) : Parcelable