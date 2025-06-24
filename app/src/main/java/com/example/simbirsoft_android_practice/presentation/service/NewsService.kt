package com.example.simbirsoft_android_practice.presentation.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.simbirsoft_android_practice.di.appComponent
import com.example.simbirsoft_android_practice.domain.model.NewsItem
import com.example.simbirsoft_android_practice.domain.repository.EventRepository
import com.example.simbirsoft_android_practice.presentation.news.NewsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG_NEWS_SERVICE = "NewsService"

class NewsService : Service() {
    private val binder = LocalBinder()

    @Inject
    lateinit var eventRepository: EventRepository

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun onBind(intent: Intent): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService(): NewsService = this@NewsService
    }

    fun loadAndFilterNews(selectedCategoryIds: Set<Int>): Flow<List<NewsItem>> {
        return eventRepository.getEvents(null)
            .flowOn(Dispatchers.IO)
            .map { events ->
                events
                    .filter { event -> event.categoryIds.any { id -> id in selectedCategoryIds } }
                    .map(NewsMapper::eventToNewsItem)
            }
            .catch { exception ->
                Log.e(
                    TAG_NEWS_SERVICE,
                    "News flow exception: ${exception.localizedMessage}",
                    exception,
                )
                emit(emptyList())
            }
    }
}
