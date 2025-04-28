package com.example.simbirsoft_android_practice.core

import com.example.simbirsoft_android_practice.data.News
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.util.concurrent.TimeUnit

private const val NEWS_JSON_FILE = "news.json"
private const val TIMEOUT_IN_MILLIS = 5_000L

class NewsRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()
    private var cachedNews: List<News>? = null

    fun getNewsObservable(): Observable<List<News>> {
        return if (cachedNews != null) {
            getNewsFromCache()
        } else {
            getNewsFromStorage()
        }
    }

    private fun getNewsFromCache(): Observable<List<News>> {
        val news = cachedNews ?: return Observable.empty()
        return Observable.just(news)
    }

    private fun getNewsFromStorage(): Observable<List<News>> {
        return Observable.fromCallable {
            val json = extractor.readJsonFile(NEWS_JSON_FILE)
            val type = object : TypeToken<List<News>>() {}.type
            gson.fromJson<List<News>>(json, type).also { loadedNews ->
                cachedNews = loadedNews
            }
        }.delaySubscription(TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
    }

    fun getNewsFlow(): Flow<List<News>> {
        return if (cachedNews != null) {
            getNewsFromCacheFlow()
        } else {
            getNewsFromStorageFlow()
        }
    }

    private fun getNewsFromCacheFlow(): Flow<List<News>> =
        flowOf(cachedNews ?: error("News cache is empty"))

    private fun getNewsFromStorageFlow(): Flow<List<News>> =
        flow {
            delay(TIMEOUT_IN_MILLIS)
            val json = extractor.readJsonFile(NEWS_JSON_FILE)
            val type = object : TypeToken<List<News>>() {}.type
            gson.fromJson<List<News>>(json, type).also { loadedNews ->
                cachedNews = loadedNews
                emit(loadedNews)
            }
        }
}
