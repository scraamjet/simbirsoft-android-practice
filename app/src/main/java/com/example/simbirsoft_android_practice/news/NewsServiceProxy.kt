package com.example.simbirsoft_android_practice.news

import com.example.simbirsoft_android_practice.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class NewsServiceProxy {
    private var newsService: NewsService? = null

    fun setService(service: NewsService) {
        this.newsService = service
    }

    fun clearService() {
        this.newsService = null
    }

    fun getNewsFlow(): Flow<List<Event>> =
        flow {
            val flow = newsService?.loadNews()
            if (flow != null) {
                emitAll(flow)
            } else {
                emit(emptyList())
            }
        }
}
