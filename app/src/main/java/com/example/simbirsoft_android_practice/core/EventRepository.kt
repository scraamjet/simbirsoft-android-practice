package com.example.simbirsoft_android_practice.core

import android.util.Log
import com.example.simbirsoft_android_practice.api.RetrofitClient.apiService
import com.example.simbirsoft_android_practice.data.Event
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

private const val TAG_EVENT_REPOSITORY = "EventRepository"
private const val NEWS_JSON_FILE = "events.json"
private const val TIMEOUT_IN_MILLIS = 5_000L

class EventRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()
    private var cachedEvents: List<Event>? = null

    fun getEventsObservable(categoryId: Int?): Observable<List<Event>> {
        return if (cachedEvents != null) {
            getEventsFromCache()
        } else {
            getEventsFromRemote(categoryId)

        }
    }

    private fun getEventsFromCache(): Observable<List<Event>> {
        val news = cachedEvents ?: return Observable.empty()
        return Observable.just(news)
    }

    private fun getEventsFromRemote(categoryId: Int?): Observable<List<Event>> {
        val body = categoryId?.let { id -> mapOf("id" to id) } ?: emptyMap()
        return apiService.getEvents(body)
            .doOnNext { events -> cachedEvents = events }
            .onErrorResumeNext { throwable: Throwable ->
                Log.w(
                    TAG_EVENT_REPOSITORY,
                    "Remote fetch failed: ${throwable.message}, loading from storage"
                )
                getEventsFromStorage()
            }
    }

    private fun getEventsFromStorage(): Observable<List<Event>> {
        return Observable.fromCallable {
            val json = extractor.readJsonFile(NEWS_JSON_FILE)
            val type = object : TypeToken<List<Event>>() {}.type
            gson.fromJson<List<Event>>(json, type).also { loadedEvent ->
                cachedEvents = loadedEvent
            }
        }.delaySubscription(TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
    }
}
