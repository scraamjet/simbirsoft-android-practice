package com.example.simbirsoft_android_practice.core

import android.util.Log
import com.example.simbirsoft_android_practice.data.News
import com.example.simbirsoft_android_practice.utils.generateRandomString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

private const val NEWS_JSON_FILE = "news.json"
private const val TIMEOUT_IN_MILLIS = 5_000L
private const val TAG = "NewsRepository"

class NewsRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()

    private fun getNews(): Observable<List<News>> {
        return Observable.fromCallable {
            val json = extractor.readJsonFile(NEWS_JSON_FILE)
            val type = object : TypeToken<List<News>>() {}.type
            gson.fromJson(json, type)
        }
    }

    fun getZippedNews(): Observable<Pair<List<News>, String>> {
        val newsObservable = getNews()
            .doOnSubscribe {
                Log.d(TAG, "Subscribed to news on thread: ${Thread.currentThread().name}")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d(TAG, "Emitted news on thread: ${Thread.currentThread().name}")
            }

        val randomStringObservable = Observable.fromCallable { generateRandomString() }
            .doOnSubscribe {
                Log.d(TAG, "Subscribed to random string on thread: ${Thread.currentThread().name}")
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d(TAG, "Emitted random string on thread: ${Thread.currentThread().name}")
            }

        return Observable.zip(newsObservable, randomStringObservable) { newsList, randomString ->
            Log.d(TAG, "Zipped on thread: ${Thread.currentThread().name}")
            Pair(newsList, randomString)
        }
    }
    fun getZippedNewsWithDelay(): Observable<Pair<List<News>, String>> {
        return getZippedNews()
            .delay(TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
    }

}
