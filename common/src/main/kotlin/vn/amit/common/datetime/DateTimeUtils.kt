package vn.amit.common.datetime

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun getDateTimeFromMs(ms: Long): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault())
}

fun formatDateTime(dateTime: LocalDateTime, format: String? = null): String {
    return dateTime.format(DateTimeFormatter.ofPattern(format ?: "HH:mm dd/MM/yyyy"))
}

fun startOfMonth(dateTime: LocalDateTime): LocalDateTime {
    return LocalDateTime.of(dateTime.year, dateTime.month, 1, 0, 0, 0)
}

fun startOfDay(dateTime: LocalDateTime): LocalDateTime {
    return LocalDateTime.of(dateTime.year, dateTime.month, dateTime.dayOfMonth, 0, 0, 0)
}