package com.example.simbirsoft_android_practice.news

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.core.NewsRepository
import com.example.simbirsoft_android_practice.data.News
import java.util.concurrent.Executors

private const val TIMEOUT_IN_MILLIS = 5_000L

class NewsService : Service() {
    private val binder = LocalBinder()
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private val newsRepository by lazy { NewsRepository(JsonAssetExtractor(this)) }

    override fun onBind(intent: Intent): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService(): NewsService = this@NewsService
    }

    fun loadNews(newsLoadedListener: (loadedNews: List<News>) -> Unit) {
        executor.execute {
            Thread.sleep(TIMEOUT_IN_MILLIS)
            val newsList = newsRepository.getNews()
            handler.post {
                newsLoadedListener(newsList)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}
