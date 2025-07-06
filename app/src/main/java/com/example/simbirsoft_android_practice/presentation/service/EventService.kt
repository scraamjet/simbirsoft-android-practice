package com.example.simbirsoft_android_practice.presentation.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.simbirsoft_android_practice.di.appComponent
import com.example.simbirsoft_android_practice.domain.model.Event
import com.example.simbirsoft_android_practice.domain.repository.EventRepository
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
            .catch { exception: Throwable ->
                Log.e(TAG_EVENT_SERVICE, "News flow exception: ${exception.message}", exception)
                emit(emptyList())
            }
    }
}

