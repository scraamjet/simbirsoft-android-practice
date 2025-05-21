package com.example.simbirsoft_android_practice.search

import com.example.simbirsoft_android_practice.model.Event
import com.example.simbirsoft_android_practice.model.SearchEvent

object SearchMapper {
    fun toSearchEvent(news: Event): SearchEvent =
        SearchEvent(
            id = news.id,
            title = news.name,
        )
}
