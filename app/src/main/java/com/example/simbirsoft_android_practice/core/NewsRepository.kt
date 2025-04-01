package com.example.simbirsoft_android_practice.core

import android.util.Log
import com.example.simbirsoft_android_practice.data.News
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val NEWS_JSON_FILE = "news.json"

class NewsRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()

    fun getNews(): Observable<List<News>> {
        return Observable.fromCallable {
            val json = extractor.readJsonFile(NEWS_JSON_FILE)
            val type = object : TypeToken<List<News>>() {}.type
            gson.fromJson<List<News>>(json, type)
        }
            .subscribeOn(Schedulers.io())
            .doOnNext { Log.d("RxJava", "Fetched news on thread: ${Thread.currentThread().name}") }
    }
}
