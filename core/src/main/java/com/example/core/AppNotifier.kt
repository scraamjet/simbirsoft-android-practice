package com.example.core

import android.content.Context

fun interface AppNotifier {
    fun makeStatusNotification(
        context: Context,
        newsId: Int,
        eventName: String,
        amount: Int,
        typeNotification: TypeNotification,
    )
}