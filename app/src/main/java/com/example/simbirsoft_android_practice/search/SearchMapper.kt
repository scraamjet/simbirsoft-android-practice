package com.example.simbirsoft_android_practice.search

import com.example.simbirsoft_android_practice.data.Event
import com.example.simbirsoft_android_practice.data.SearchEvent

object SearchMapper {
    fun toSearchEvent(news: Event): SearchEvent =
        SearchEvent(
            id = news.id,
            title = news.name,
        )
}
