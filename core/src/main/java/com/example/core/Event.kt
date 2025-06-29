package com.example.core

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("startDate")
    val startDate: Long,
    @SerializedName("endDate")
    val endDate: Long,
    @SerializedName("description")
    val description: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("photos")
    val photos: List<String>,
    @SerializedName("category")
    val categoryIds: List<Int>,
    @SerializedName("createAt")
    val createAt: Long,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("organisation")
    val organisation: String,
)
