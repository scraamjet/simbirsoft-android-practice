package com.example.news

import com.example.core.model.Event
import com.example.core.model.NewsItem

object NewsMapper {
    fun eventToNewsItem(event: Event): NewsItem =
        NewsItem(
            id = event.id,
            title = event.name,
            description = event.description,
            startDateTime = event.startDate,
            endDateTime = event.endDate,
            imageUrl = event.photos.first(),
        )

    fun eventToNewsDetail(event: Event): NewsDetail =
        NewsDetail(
            title = event.name,
            fullDescription = event.description,
            startDateTime = event.startDate,
            endDateTime = event.endDate,
            owner = event.organisation,
            ownerAddress = event.address,
            ownerContacts = event.phone,
            picturesUrl = event.photos,
        )
}
