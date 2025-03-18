package com.example.simbirsoft_android_practice.utils

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
    private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM", Locale("ru"))

    fun formatEventDates(
        startDate: String,
        endDate: String,
    ): String {
        val start = convertTimestampToLocalDate(startDate)
        val end = convertTimestampToLocalDate(endDate)
        val now = Clock.System.todayIn(TimeZone.currentSystemDefault())

        val daysLeft = now.daysUntil(start)
        val dateRange = formatDateRange(start, end)

        return if (daysLeft >= 0) {
            "Осталось ${formatDaysText(daysLeft)} $dateRange"
        } else {
            "Событие завершено $dateRange"
        }
    }

    private fun convertTimestampToLocalDate(dateString: String): LocalDate {
        return dateString.toLongOrNull()
            ?.let { timestamp -> convertTimestampToLocalDate(timestamp) }
            ?: LocalDate.parse(dateString)
    }

    private fun formatDateRange(
        start: LocalDate,
        end: LocalDate,
    ): String {
        return "(${formatDate(start)} – ${formatDate(end)})"
    }

    private fun formatDaysText(days: Int): String {
        val word =
            when {
                days % 10 == 1 && days % 100 != 11 -> "день"
                days % 10 in 2..4 && days % 100 !in 12..14 -> "дня"
                else -> "дней"
            }
        return "$days $word"
    }

    private fun convertTimestampToLocalDate(timestamp: Long): LocalDate {
        return Instant.fromEpochSeconds(timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    private fun formatDate(date: LocalDate): String {
        return date.toJavaLocalDate().format(DATE_FORMATTER)
    }
}
