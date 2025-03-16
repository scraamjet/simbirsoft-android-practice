package com.example.simbirsoft_android_practice

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("id")
    val id: Int,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("owner_address")
    val ownerAddress: String,
    @SerializedName("owner_contacts")
    val ownerContacts: String,
    @SerializedName("pictures_url")
    val picturesUrl: List<String>,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("full_description")
    val fullDescription: String,
    @SerializedName("start_date_time")
    val startDateTime: String,
    @SerializedName("end_date_time")
    val endDateTime: String,
    @SerializedName("list_help_category_id")
    val listHelpCategoryId: List<Int>
) {
    fun toNewsItem(): NewsItem {
        return NewsItem(
            id = this.id,
            title = this.title,
            description = this.description,
            startDateTime = this.startDateTime,
            endDateTime = this.endDateTime,
            imageUrl = this.picturesUrl.firstOrNull()
        )
    }

    fun toNewsDetail(): NewsDetail {
        return NewsDetail(
            title = this.title,
            fullDescription = this.fullDescription,
            startDateTime = this.startDateTime,
            endDateTime = this.endDateTime,
            owner = this.owner,
            ownerAddress = this.ownerAddress,
            ownerContacts = this.ownerContacts,
            picturesUrl = this.picturesUrl
        )
    }
}
