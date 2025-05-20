package com.example.simbirsoft_android_practice.core

import android.util.Log
import com.example.simbirsoft_android_practice.api.RetrofitClient.apiService
import com.example.simbirsoft_android_practice.data.Event
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

private const val TAG_EVENT_REPOSITORY = "EventRepository"
private const val NEWS_JSON_FILE = "events.json"
private const val TIMEOUT_IN_MILLIS = 5_000L

class EventRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()
    private var cachedEvents: List<Event>? = null

    fun getEvents(categoryId: Int?): Single<List<Event>> {
        return if (cachedEvents != null) {
            getEventsFromCache()
        } else {
            getEventsFromRemote(categoryId)
        }
    }

    private fun getEventsFromCache(): Single<List<Event>> {
        val events = cachedEvents ?: return Single.error(IllegalStateException("Cache is empty"))
        return Single.just(events)
    }

    private fun getEventsFromRemote(categoryId: Int?): Single<List<Event>> {
        val body = categoryId?.let { id -> mapOf("id" to id) } ?: emptyMap()
        return apiService.getEvents(body)
            .doOnSuccess { events -> cachedEvents = events }
            .onErrorResumeNext { throwable: Throwable ->
                Log.w(
                    TAG_EVENT_REPOSITORY,
                    "Remote fetch failed: ${throwable.message}, loading from storage",
                )
                getEventsFromStorage()
            }
    }

    private fun getEventsFromStorage(): Single<List<Event>> {
        return Single.fromCallable {
            val json = extractor.readJsonFile(NEWS_JSON_FILE)
            val type = object : TypeToken<List<Event>>() {}.type
            gson.fromJson<List<Event>>(json, type).also { loadedEvents ->
                cachedEvents = loadedEvents
            }
        }.delaySubscription(TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
    }
}
