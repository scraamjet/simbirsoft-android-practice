package com.example.simbirsoft_android_practice.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterCategory(
    val id: Int,
    val title: String,
    var isEnabled: Boolean = true,
) : Parcelable
