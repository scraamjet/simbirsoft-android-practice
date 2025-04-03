package com.example.simbirsoft_android_practice.news

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.core.NewsRepository
import com.example.simbirsoft_android_practice.data.News
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

private const val TIMEOUT_IN_MILLIS = 5_000L

class NewsService : Service() {
    private val binder = LocalBinder()
    private val newsRepository by lazy { NewsRepository(JsonAssetExtractor(this)) }

    override fun onBind(intent: Intent): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService(): NewsService = this@NewsService
    }

    @SuppressLint("CheckResult")
    fun loadNews(newsLoadedListener: (List<News>) -> Unit) {
        newsRepository.getNews()
            .delay(TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { loadedNews ->
                newsLoadedListener(loadedNews)
            }
    }
}

