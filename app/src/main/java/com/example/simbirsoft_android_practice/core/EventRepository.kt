package com.example.simbirsoft_android_practice.core

import android.util.Log
import com.example.simbirsoft_android_practice.api.RetrofitClient.apiService
import com.example.simbirsoft_android_practice.data.Event
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

private const val TAG_EVENT_REPOSITORY = "EventRepository"
private const val NEWS_JSON_FILE = "events.json"
private const val TIMEOUT_IN_MILLIS = 5_000L

class EventRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()
    private var cachedEvents: List<Event>? = null

    fun getEventsFlow(categoryId: Int?): Flow<List<Event>> {
        return if (cachedEvents != null) {
            getEventsFromCacheFlow()
        } else {
            getEventsFromRemoteFlow(categoryId)
        }
    }

    private fun getEventsFromRemoteFlow(categoryId: Int?): Flow<List<Event>> {
        val body = categoryId?.let { id -> mapOf("id" to id) } ?: emptyMap()
        return flow {
            val events = apiService.getEvents(body).blockingFirst()
            cachedEvents = events
            emit(events)
        }.catch { error ->
            Log.w(TAG_EVENT_REPOSITORY, "Remote fetch failed: ${error.message}, loading from storage")
            emitAll(getEventsFromStorageFlow())
        }
    }

    private fun getEventsFromCacheFlow(): Flow<List<Event>> =
        flowOf(cachedEvents ?: error("News cache is empty"))

    private fun getEventsFromStorageFlow(): Flow<List<Event>> =
        flow {
            delay(TIMEOUT_IN_MILLIS)
            val json = extractor.readJsonFile(NEWS_JSON_FILE)
            val type = object : TypeToken<List<Event>>() {}.type
            gson.fromJson<List<Event>>(json, type).also { loadedNews ->
                cachedEvents = loadedNews
                emit(loadedNews)
            }
        }
}
