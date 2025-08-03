package com.example.background.di

import com.example.core.notification.AppNotifier

interface WorkerComponentProvider {
    fun provideWorkerComponent(): WorkerComponent
    fun provideAppNotifier(): AppNotifier
}