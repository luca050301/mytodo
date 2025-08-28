package com.example.mytodo.utils

import java.time.LocalDateTime

class DateUtils {
    companion object {
        fun formatDate(localDateTime: LocalDateTime): String {
            val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
            val month = localDateTime.monthValue.toString().padStart(2, '0')
            val year = localDateTime.year.toString()
            return "$day.$month.$year"
        }

        fun millisToLocalDateTime(millis: Long): LocalDateTime {
            return LocalDateTime.ofEpochSecond(millis / 1000, 0, java.time.ZoneOffset.UTC)
        }

        fun localDateTimeToMillis(dateTime: LocalDateTime): Long {
            return dateTime.toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
        }

    }
}