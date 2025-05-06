package com.example.simbirsoft_android_practice.news

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.simbirsoft_android_practice.core.RepositoryProvider
import com.example.simbirsoft_android_practice.data.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

private const val TAG_NEWS_SERVICE = "NewsService"

class NewsService : Service() {
    private val binder = LocalBinder()
    private val eventRepository by lazy {
        RepositoryProvider.fromContext(applicationContext).eventRepository
    }

    override fun onBind(intent: Intent): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService(): NewsService = this@NewsService
    }

    fun loadNews(newsLoadedListener: (List<Event>) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            eventRepository.getEventsFlow(null)
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    Log.e(
                        TAG_NEWS_SERVICE,
                        "Flow exception: ${throwable.localizedMessage}",
                        throwable
                    )
                }
                .collect { news ->
                    newsLoadedListener(news)
                }
        }
    }

}
