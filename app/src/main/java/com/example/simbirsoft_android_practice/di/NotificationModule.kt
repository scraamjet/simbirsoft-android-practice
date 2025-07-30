package com.example.simbirsoft_android_practice.di

import com.example.core.NotificationComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotificationModule {
    @Provides
    @Singleton
    fun provideNotificationComponent(): NotificationComponent {
        return NotificationComponentImpl()
    }
}