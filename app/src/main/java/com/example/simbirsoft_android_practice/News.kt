package com.example.simbirsoft_android_practice

import com.google.gson.annotations.SerializedName

data class News(
    val id: Int,
    val owner: String,
    @SerializedName("owner_address")
    val ownerAddress: String,
    @SerializedName("owner_contacts")
    val ownerContacts: String,
    @SerializedName("pictures_url")
    val picturesUrl: List<String>,
    val title: String,
    val description: String,
    @SerializedName("full_description")
    val fullDescription: String,
    @SerializedName("start_date_time")
    val startDateTime: String,
    @SerializedName("end_date_time")
    val endDateTime: String,
    @SerializedName("list_help_category_id")
    val listHelpCategoryId: List<Int>
)
