package com.example.simbirsoft_android_practice.core

import com.example.simbirsoft_android_practice.data.Event
import com.example.simbirsoft_android_practice.data.News
import com.example.simbirsoft_android_practice.search.SearchMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Single

class EventRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()
    private companion object {
        const val EVENTS_JSON_FILE = "news.json"
    }

    fun getEvents(): Single<List<Event>> {
        return Single.fromCallable {
            val json = extractor.readJsonFile(EVENTS_JSON_FILE)
            val type = object : TypeToken<List<News>>() {}.type
            val newsList: List<News> = gson.fromJson(json, type)
            newsList.map(SearchMapper::toEvent)
        }
    }
}