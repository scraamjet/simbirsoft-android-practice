package com.example.simbirsoft_android_practice.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context = context.applicationContext

    @Provides
    @Singleton
    fun provideWorkManager(context: Context): WorkManager = WorkManager.getInstance(context)

}
