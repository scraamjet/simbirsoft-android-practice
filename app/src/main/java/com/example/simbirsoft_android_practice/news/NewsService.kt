package com.example.simbirsoft_android_practice.news

import android.app.Service
import android.content.Intent
import android.os.IBinder

import android.os.Binder
import android.os.Handler
import android.os.Looper
import com.example.simbirsoft_android_practice.core.JsonParser
import com.example.simbirsoft_android_practice.data.News
import java.util.concurrent.Executors

class NewsService : Service() {
    private val binder = LocalBinder()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var jsonParser: JsonParser

    override fun onCreate() {
        super.onCreate()
        jsonParser = JsonParser(this)
    }

    override fun onBind(intent: Intent): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService(): NewsService = this@NewsService
    }

    fun loadNews(callback: (List<News>) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            Thread.sleep(5000)
            val news = jsonParser.parseNews()
            handler.post { callback(news) }
        }
    }
}