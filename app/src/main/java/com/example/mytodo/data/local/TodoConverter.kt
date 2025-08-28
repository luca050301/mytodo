package com.example.mytodo.data.local

import androidx.room.TypeConverter

/**
 * Converter for columns of the To-do Entity, converts date types to room friendly types
 */
class TodoConverter {
    @TypeConverter
    fun timestampLongToInstant(value: Long?): java.time.Instant? {
        return value?.let { java.time.Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun instantToTimestampLong(instant: java.time.Instant?): Long? {
        return instant?.toEpochMilli()
    }

    @TypeConverter
    fun localDateTimeToString(dateTime: java.time.LocalDateTime?): String? {
        return dateTime?.toString()
    }

    @TypeConverter
    fun stringToLocalDateTime(value: String?): java.time.LocalDateTime? {
        return value?.let { java.time.LocalDateTime.parse(it) }
    }
}