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

private const val TAG_NEWS_SERVICE = "NewsService"
private const val TAG_RANDOM_STRING = "RandomString"

class NewsService : Service() {
    private val binder = LocalBinder()
    private val newsRepository by lazy { NewsRepository(JsonAssetExtractor(this)) }

    override fun onBind(intent: Intent): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService(): NewsService = this@NewsService
    }

    @SuppressLint("CheckResult")
    fun loadNews(newsLoadedListener: (List<News>) -> Unit): Disposable {
        return newsRepository.getZippedNewsWithDelay()
            .doOnSubscribe {
                Log.d(TAG_NEWS_SERVICE, "Subscribed on thread: ${Thread.currentThread().name}")
            }
            .subscribeOn(Schedulers.io())
            .doOnNext { (_, randomString) ->
                Log.d(TAG_RANDOM_STRING, "Generated random string: $randomString")
            }
            .map { (newsList, _) ->
                newsList
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d(TAG_NEWS_SERVICE, "Received news on thread: ${Thread.currentThread().name}")
            }
            .subscribe { loadedNews -> newsLoadedListener(loadedNews) }
    }
}
