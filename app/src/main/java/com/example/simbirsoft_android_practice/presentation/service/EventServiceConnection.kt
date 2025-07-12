package com.example.simbirsoft_android_practice.presentation.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class EventServiceConnection(
    private val onServiceConnected: (EventService) -> Unit,
    private val onServiceDisconnected: () -> Unit,
) : ServiceConnection {
    override fun onServiceConnected(
        className: ComponentName,
        service: IBinder,
    ) {
        val eventService = (service as EventService.LocalBinder).getService()
        onServiceConnected(eventService)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        onServiceDisconnected()
    }
}
