package com.example.worker

import com.example.core.AppNotifier

interface WorkerComponentProvider {
    fun provideWorkerComponent(): WorkerComponent
    fun provideAppNotifier(): AppNotifier
}