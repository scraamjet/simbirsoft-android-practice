package com.example.simbirsoft_android_practice.news

import com.example.simbirsoft_android_practice.data.News
import com.example.simbirsoft_android_practice.data.NewsDetail
import com.example.simbirsoft_android_practice.data.NewsItem

object NewsMapper {
    fun toNewsItem(news: News): NewsItem {
        return NewsItem(
            id = news.id,
            title = news.title,
            description = news.description,
            startDateTime = news.startDateTime,
            endDateTime = news.endDateTime,
            imageUrl =  news.picturesUrl.first()
        )
    }

    fun toNewsDetail(news: News): NewsDetail {
        return NewsDetail(
            title = news.title,
            fullDescription = news.fullDescription,
            startDateTime = news.startDateTime,
            endDateTime = news.endDateTime,
            owner = news.owner,
            ownerAddress = news.ownerAddress,
            ownerContacts = news.ownerContacts,
            picturesUrl = news.picturesUrl
        )
    }
}