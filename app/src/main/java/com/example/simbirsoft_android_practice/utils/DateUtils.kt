package com.example.simbirsoft_android_practice.utils

import android.content.Context
import com.example.simbirsoft_android_practice.R
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM", Locale("ru"))

    fun formatEventDates(
        context: Context,
        startDateTime: Long,
        endDateTime: Long,
    ): String {
        val start = convertTimestampToLocalDate(startDateTime)
        val end = convertTimestampToLocalDate(endDateTime)
        val now = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val daysLeft = now.daysUntil(start)

        val dateRange =
            if (start == end) {
                context.getString(R.string.date_single, formatDate(start))
            } else {
                context.getString(R.string.date_range, formatDate(start), formatDate(end))
            }

        return when {
            now < start ->
                context.getString(
                    R.string.event_upcoming,
                    formatDaysText(context, daysLeft),
                    dateRange,
                )

            now in start..end -> context.getString(R.string.event_today, dateRange)
            else -> context.getString(R.string.event_finished, dateRange)
        }
    }

    private fun formatDaysText(
        context: Context,
        days: Int,
    ): String {
        return context.resources.getQuantityString(R.plurals.days, days, days)
    }

    private fun formatDate(date: LocalDate): String {
        return date.toJavaLocalDate().format(dateFormatter)
    }

    private fun convertTimestampToLocalDate(timestamp: Long): LocalDate {
        return Instant.fromEpochSeconds(timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
}
