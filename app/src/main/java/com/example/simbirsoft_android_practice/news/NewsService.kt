package com.example.simbirsoft_android_practice.news

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.core.NewsRepository
import com.example.simbirsoft_android_practice.data.News
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NewsService : Service() {
    private val binder = LocalBinder()
    private val newsRepository by lazy { NewsRepository(JsonAssetExtractor(this)) }

    override fun onBind(intent: Intent): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService(): NewsService = this@NewsService
    }

    @SuppressLint("CheckResult")
    fun loadNews(newsLoadedListener: (List<News>) -> Unit): Disposable {
        return newsRepository.getNewsWithDelay()
            .doOnSubscribe {
                Log.d(
                    "NewsService",
                    "Subscribed on thread: ${Thread.currentThread().name}"
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d(
                    "NewsService",
                    "Received news on thread: ${Thread.currentThread().name}"
                )
            }
            .subscribe { loadedNews -> newsLoadedListener(loadedNews) }
    }
}

