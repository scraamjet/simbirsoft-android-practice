package com.example.worker

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [NotificationModule::class])
interface NotificationComponent {

    fun notificationManager(): NotificationManagerCompat

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance @Named("worker_context") context: Context): NotificationComponent
    }
}
