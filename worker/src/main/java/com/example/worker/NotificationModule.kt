package com.example.worker

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class NotificationModule {

    @Provides
    fun provideNotificationManager(@Named("worker_context")context: Context): NotificationManagerCompat {
        return NotificationManagerCompat.from(context)
    }
}
