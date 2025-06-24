package com.example.simbirsoft_android_practice.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HelpCategory(
    val id: Int,
    val title: String,
    val iconUrl: String,
) : Parcelable
