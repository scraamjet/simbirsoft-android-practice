package com.example.core.mapper

import com.example.core.model.Event
import com.example.core.model.SearchEvent

object SearchMapper {
    fun toSearchEvent(news: Event): SearchEvent =
        SearchEvent(
            id = news.id,
            title = news.name,
        )
}
