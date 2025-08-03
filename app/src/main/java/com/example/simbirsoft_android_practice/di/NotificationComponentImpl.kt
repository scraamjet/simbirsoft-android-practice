package com.example.simbirsoft_android_practice.di

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.example.core.DonateWorkerKeys
import com.example.core.NotificationComponent
import com.example.core.TypeNotification
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.presentation.main.MainActivity
import com.example.worker.RemindLaterReceiver

private const val DONATE_CHANNEL_ID = "donate_channel"
private const val DONATE_CHANNEL_NAME = "Пожертвования"
private const val REMINDER_CHANNEL_ID = "reminder_channel"
private const val REMINDER_CHANNEL_NAME = "Напоминания"
private const val NEWS_ID_DIALOG_KEY = "newsId"
private const val NOTIFICATION_REMINDER_OFFSET = 10_000

class NotificationComponentImpl : NotificationComponent {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun makeStatusNotification(
        context: Context,
        newsId: Int,
        eventName: String,
        amount: Int,
        typeNotification: TypeNotification,
    ) {
        val notificationManager = NotificationManagerCompat.from(context)

        val donateChannel = NotificationChannel(
            DONATE_CHANNEL_ID,
            DONATE_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val reminderChannel = NotificationChannel(
            REMINDER_CHANNEL_ID,
            REMINDER_CHANNEL_NAME,
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
            .setArguments(bundleOf(NEWS_ID_DIALOG_KEY to newsId))
            .createPendingIntent()

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_logo)
            .setContentTitle(eventName)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    when (typeNotification) {
                        TypeNotification.SEND_NOTIFICATION ->
                            context.getString(R.string.notification_donate_text, amount)

                        TypeNotification.REMINDER_NOTIFICATION ->
                            context.getString(R.string.notification_reminder_text)
                    }
                )
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (typeNotification == TypeNotification.SEND_NOTIFICATION) {
            val remindIntent = PendingIntent.getBroadcast(
                context,
                newsId,
                Intent(context, RemindLaterReceiver::class.java).apply {
                    putExtra(DonateWorkerKeys.NEWS_ID, newsId)
                    putExtra(DonateWorkerKeys.NEWS_TITLE, eventName)
                    putExtra(DonateWorkerKeys.AMOUNT, amount)
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            builder.addAction(
                R.drawable.history_icon,
                context.getString(R.string.notification_action_remind),
                remindIntent
            )
        }

        notificationManager.notify(
            newsId + if (typeNotification == TypeNotification.REMINDER_NOTIFICATION) {
                NOTIFICATION_REMINDER_OFFSET
            } else {
                0
            },
            builder.build()
        )
    }
}
