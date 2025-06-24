package com.example.simbirsoft_android_practice.core

import android.util.Log
import com.example.simbirsoft_android_practice.data.api.ApiService
import com.example.simbirsoft_android_practice.database.EventDao
import com.example.simbirsoft_android_practice.database.EventEntityMapper
import com.example.simbirsoft_android_practice.domain.model.Event
import com.example.simbirsoft_android_practice.domain.repository.EventRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG_EVENT_REPOSITORY = "EventRepository"
private const val EVENTS_JSON_FILE = "events.json"
private const val TIMEOUT_IN_MILLIS = 5_000L

class EventRepositoryImpl @Inject constructor(
    private val extractor: JsonAssetExtractor,
    private val eventDao: EventDao,
    private val gson: Gson,
    private val apiService: ApiService,
    ) : EventRepository {
        private var isDataLoaded = false

    override fun getEvents(categoryId: Int?): Flow<List<Event>> {
            return if (isDataLoaded) {
                getEventsFromDatabase()
            } else {
                getEventsFromRemote(categoryId)
            }
        }

        private fun getEventsFromRemote(categoryId: Int?): Flow<List<Event>> =
            flow {
                val body =
                    categoryId?.let { id -> mapOf("id" to id) } ?: emptyMap()
                val fetchedEvents = apiService.getEvents(body)

                val (entities, crossRefs) = EventEntityMapper.fromEventList(fetchedEvents)
                eventDao.insertEvents(entities)
                eventDao.insertEventCategoryCrossRefs(crossRefs)

                emit(fetchedEvents)

                isDataLoaded = true
            }.catch { throwable ->
                Log.w(
                    TAG_EVENT_REPOSITORY,
                    "Remote fetch failed: ${throwable.message}, loading from storage",
                )
                emitAll(getEventsFromJson())
            }

        private fun getEventsFromJson(): Flow<List<Event>> =
            flow {
                delay(TIMEOUT_IN_MILLIS)

                val json = extractor.readJsonFile(EVENTS_JSON_FILE)
                val type = object : TypeToken<List<Event>>() {}.type
                val loadedEvents = gson.fromJson<List<Event>>(json, type)

                val (entities, crossRefs) = EventEntityMapper.fromEventList(loadedEvents)
                eventDao.insertEvents(entities)
                eventDao.insertEventCategoryCrossRefs(crossRefs)

                emit(loadedEvents)

                isDataLoaded = true
            }

        private fun getEventsFromDatabase(): Flow<List<Event>> {
            return eventDao.getAllEvents()
                .map { eventsWithCategories -> EventEntityMapper.toEventList(eventsWithCategories) }
        }
    }
