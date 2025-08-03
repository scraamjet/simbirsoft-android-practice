package com.example.worker

import com.example.core.notification.AppNotifier

interface WorkerComponentProvider {
    fun provideWorkerComponent(): WorkerComponent
    fun provideAppNotifier(): AppNotifier
}