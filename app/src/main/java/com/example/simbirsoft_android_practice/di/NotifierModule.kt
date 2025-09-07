package com.example.simbirsoft_android_practice.di

import com.example.core.notification.AppNotifier
import com.example.simbirsoft_android_practice.notification.AppNotifierImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotifierModule {
    @Provides
    @Singleton
    fun provideAppNotifier(): AppNotifier {
        return AppNotifierImpl()
    }
}