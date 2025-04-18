package com.example.simbirsoft_android_practice.core

import com.example.simbirsoft_android_practice.data.News
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

private const val NEWS_JSON_FILE = "news.json"
private const val TIMEOUT_IN_MILLIS = 5_000L

class NewsRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()
    private var cachedNews: List<News>? = null

    fun getCachedNews(): List<News>? = cachedNews

    fun getNewsFromCache(): Observable<List<News>> {
        return cachedNews?.let { cachedNewsList ->
            Observable.just(cachedNewsList)
        } ?: run {
            Observable.empty()
        }
    }

    fun getNewsWithDelay(): Observable<List<News>> {
        return Observable.fromCallable {
            val json = extractor.readJsonFile(NEWS_JSON_FILE)
            val type = object : TypeToken<List<News>>() {}.type
            gson.fromJson<List<News>>(json, type).also { loadedNews ->
                cachedNews = loadedNews
            }
        }
            .delaySubscription(TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
    }
}

