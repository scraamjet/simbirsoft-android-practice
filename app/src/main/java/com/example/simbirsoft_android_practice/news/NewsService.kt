package com.example.simbirsoft_android_practice.news

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.core.NewsRepository
import com.example.simbirsoft_android_practice.data.News
import io.reactivex.rxjava3.core.Observable

class NewsService : Service() {
    private val binder = LocalBinder()
    private val newsRepository by lazy { NewsRepository(JsonAssetExtractor(this)) }

    override fun onBind(intent: Intent): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService(): NewsService = this@NewsService
    }

    fun loadNews(): Observable<List<News>> {
        return newsRepository.getNews()
    }
}
