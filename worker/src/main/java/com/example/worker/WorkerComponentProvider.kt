package com.example.worker

import com.example.core.NotificationComponent

interface WorkerComponentProvider {
    fun provideWorkerComponent(): WorkerComponent
    fun provideNotificationComponent(): NotificationComponent
}