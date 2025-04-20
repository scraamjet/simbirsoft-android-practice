package com.example.simbirsoft_android_practice.news

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.simbirsoft_android_practice.core.RepositoryProvider
import com.example.simbirsoft_android_practice.data.News
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val TAG_NEWS_SERVICE = "NewsService"

class NewsService : Service() {
    private val binder = LocalBinder()
    private val newsRepository by lazy {
        (applicationContext as RepositoryProvider).newsRepository
    }

    override fun onBind(intent: Intent): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService(): NewsService = this@NewsService
    }

    fun isNewsAlreadyLoaded(): Boolean {
        return newsRepository.hasCachedNews()
    }

    fun loadNews(newsLoadedListener: (List<News>) -> Unit): Disposable {
        return newsRepository.getNewsFromCache()
            .doOnSubscribe {
                Log.d(TAG_NEWS_SERVICE, "Subscribed to news on thread: ${Thread.currentThread().name}")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { news ->
                Log.d(
                    TAG_NEWS_SERVICE,
                    "Emitting cached news on thread: ${Thread.currentThread().name}, count: ${news.size}"
                )
            }
            .subscribe { news -> newsLoadedListener(news) }
    }

    fun loadNewsWithDelay(newsLoadedListener: (List<News>) -> Unit): Disposable {
        return newsRepository.getNewsWithDelay()
            .doOnSubscribe {
                Log.d(TAG_NEWS_SERVICE, "Subscribed to news on thread: ${Thread.currentThread().name}")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { news ->
                Log.d(
                    TAG_NEWS_SERVICE,
                    "Emitting delayed news on thread: ${Thread.currentThread().name}, count: ${news.size}"
                )
            }
            .subscribe { news -> newsLoadedListener(news) }
    }
}
