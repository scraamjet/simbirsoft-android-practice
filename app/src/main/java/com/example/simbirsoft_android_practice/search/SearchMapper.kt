package com.example.simbirsoft_android_practice.search

import com.example.simbirsoft_android_practice.domain.model.Event
import com.example.simbirsoft_android_practice.domain.model.SearchEvent

object SearchMapper {
    fun toSearchEvent(news: Event): SearchEvent =
        SearchEvent(
            id = news.id,
            title = news.name,
        )
}
