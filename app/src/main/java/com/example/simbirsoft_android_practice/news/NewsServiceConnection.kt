package com.example.simbirsoft_android_practice.news

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class NewsServiceConnection(
    private val onServiceConnected: (NewsService) -> Unit,
    private val onServiceDisconnected: () -> Unit
) : ServiceConnection {
    override fun onServiceConnected(
        className: ComponentName,
        service: IBinder
    ) {
        val newsService = (service as NewsService.LocalBinder).getService()
        onServiceConnected(newsService)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        onServiceDisconnected()
    }
}