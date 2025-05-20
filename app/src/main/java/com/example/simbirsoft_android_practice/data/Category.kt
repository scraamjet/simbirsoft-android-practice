package com.example.simbirsoft_android_practice.data

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name_en")
    val nameEn: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String,
)
