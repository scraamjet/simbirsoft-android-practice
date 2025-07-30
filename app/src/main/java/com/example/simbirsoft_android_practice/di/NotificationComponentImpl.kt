package com.example.simbirsoft_android_practice.di

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.example.core.NotificationComponent
import com.example.core.TypeNotification
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.presentation.main.MainActivity
import com.example.worker.RemindLaterReceiver

class NotificationComponentImpl() : NotificationComponent {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun makeStatusNotification(
        context: Context,
        eventId: Int,
        eventName: String,
        amount: Int,
        typeNotification: TypeNotification,
    ) {
        Log.d("Notification", "makeStatusNotification called, amount: $amount")
        val notificationManager = NotificationManagerCompat.from(context)

        val donateChannel = NotificationChannel(
            "donate_channel",
            "Пожертвования",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val reminderChannel = NotificationChannel(
            "reminder_channel",
            "Напоминания",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val systemManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        systemManager.createNotificationChannel(donateChannel)
        systemManager.createNotificationChannel(reminderChannel)

        val channelId =
            if (typeNotification == TypeNotification.SEND_NOTIFICATION) "donate_channel" else "reminder_channel"

        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.newsDetailFragment)
            .setComponentName(MainActivity::class.java)
            .setArguments(bundleOf("newsId" to eventId))
            .createPendingIntent()

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_logo)
            .setContentTitle(eventName)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    when (typeNotification) {
                        TypeNotification.SEND_NOTIFICATION -> "Спасибо, что пожертвовали $amount ₽! Будем очень признательны, если вы сможете пожертвовать еще больше."
                        TypeNotification.REMINDER_NOTIFICATION -> "Напоминаем, что мы будем очень признательны, если вы сможете пожертвовать еще больше."
                    }
                )
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (typeNotification == TypeNotification.SEND_NOTIFICATION) {
            val remindIntent = PendingIntent.getBroadcast(
                context,
                eventId,
                Intent(context, RemindLaterReceiver::class.java).apply {
                    putExtra("news_id", eventId)
                    putExtra("news_title", eventName)
                    putExtra("amount", amount)
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            builder.addAction(R.drawable.history_icon, "Напомнить позже", remindIntent)
        }

        notificationManager.notify(
            eventId + if (typeNotification == TypeNotification.REMINDER_NOTIFICATION) 10000 else 0,
            builder.build()
        )
    }
}
