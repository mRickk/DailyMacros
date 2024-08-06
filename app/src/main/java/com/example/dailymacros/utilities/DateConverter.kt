package com.example.dailymacros.utilities

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateConverter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @TypeConverter
    fun fromDate(date: Date): String {
        return dateFormat.format(date)
    }

    @TypeConverter
    fun toDate(dateString: String): Date {
        return dateFormat.parse(dateString) ?: Date()
    }
}