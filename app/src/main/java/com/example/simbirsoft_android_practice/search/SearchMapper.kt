package com.example.simbirsoft_android_practice.search

import com.example.simbirsoft_android_practice.data.Event
import com.example.simbirsoft_android_practice.data.News

object SearchMapper {
    fun toEvent(news: News): Event =
        Event(
            id = news.id,
            title = news.title
        )
}