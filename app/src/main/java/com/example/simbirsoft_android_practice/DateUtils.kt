package com.example.simbirsoft_android_practice

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.DateTimeException
import java.time.format.DateTimeFormatter


object DateUtils {

    fun formatEventDates(startDate: String, endDate: String): String {
        val start = parseDate(startDate)
        val end = parseDate(endDate)
        val now = Clock.System.todayIn(TimeZone.currentSystemDefault())

        val daysLeft = now.daysUntil(start)
        val startFormatted = start.toJavaLocalDate().format(DateTimeFormatter.ofPattern("dd.MM"))
        val endFormatted = end.toJavaLocalDate().format(DateTimeFormatter.ofPattern("dd.MM"))

        return if (daysLeft >= 0) {
            "Осталось $daysLeft дней ($startFormatted - $endFormatted)"
        } else {
            "Событие завершено ($startFormatted - $endFormatted)"
        }
    }

    private fun parseDate(dateString: String): LocalDate {
        return try {
            LocalDate.parse(dateString)
        } catch (e: Exception) {
            try {
                val timestamp = dateString.toLongOrNull()
                timestamp?.let {
                    Instant.fromEpochSeconds(it)
                        .toLocalDateTime(TimeZone.currentSystemDefault()).date
                } ?: throw DateTimeException("Invalid date format: $dateString")
            } catch (e: Exception) {
                throw DateTimeException("Failed to parse date: $dateString")
            }
        }
    }
}
