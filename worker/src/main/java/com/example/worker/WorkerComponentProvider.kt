package com.example.worker

interface WorkerComponentProvider {
    fun provideWorkerComponent(): WorkerComponent
    fun provideNotificationComponent(): NotificationComponent
}