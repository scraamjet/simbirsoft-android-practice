package com.example.simbirsoft_android_practice.news

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.simbirsoft_android_practice.appComponent
import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val TAG_EVENT_SERVICE = "EventService"

class EventService : Service() {
    private val binder = LocalBinder()

    @Inject
    lateinit var eventRepository: EventRepository

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun onBind(intent: Intent): IBinder = binder

    inner class LocalBinder : Binder() {
        fun getService(): EventService = this@EventService
    }

    fun loadEvents(): Flow<List<Event>> {
        return eventRepository.getEvents(null)
            .flowOn(Dispatchers.IO)
            .catch { throwable ->
                Log.e(TAG_EVENT_SERVICE, "Flow exception: ${throwable.localizedMessage}", throwable)
            }
    }
}
