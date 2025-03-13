package com.example.simbirsoft_android_practice.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter


object DateUtils {

    fun formatEventDates(startDate: String, endDate: String): String {
        val start = parseDate(startDate)
        val end = parseDate(endDate)
        val now = Clock.System.todayIn(TimeZone.currentSystemDefault())

        val daysLeft = now.daysUntil(start)
        val dateRange = formatDateRange(start, end)

        return if (daysLeft >= 0) {
            "Осталось ${formatDaysText(daysLeft)} $dateRange"
        } else {
            "Событие завершено $dateRange"
        }
    }

    private fun parseDate(dateString: String): LocalDate {
        return dateString.toLongOrNull()?.let { timestamp ->
            Instant.fromEpochSeconds(timestamp)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
        } ?: LocalDate.parse(dateString)
    }

    private fun formatDateRange(start: LocalDate, end: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM")
        val startFormatted = start.toJavaLocalDate().format(formatter)
        val endFormatted = end.toJavaLocalDate().format(formatter)
        return "($startFormatted - $endFormatted)"
    }

    private fun formatDaysText(daysLeft: Int): String {
        val dayWord = when {
            daysLeft % 10 == 1 && daysLeft % 100 != 11 -> "день"
            daysLeft % 10 in 2..4 && daysLeft % 100 !in 12..14 -> "дня"
            else -> "дней"
        }
        return "$daysLeft $dayWord"
    }
}
