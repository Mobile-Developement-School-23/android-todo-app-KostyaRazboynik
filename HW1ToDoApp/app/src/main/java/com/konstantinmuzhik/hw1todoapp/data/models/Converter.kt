package com.konstantinmuzhik.hw1todoapp.data.models

import androidx.room.TypeConverter
import java.util.Date

class Converter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}