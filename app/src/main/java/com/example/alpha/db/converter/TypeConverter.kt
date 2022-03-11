package com.example.alpha.db.converter

import androidx.room.TypeConverter
import java.time.DayOfWeek

class TypeConverter {
    @TypeConverter
    fun dayOfWeekToString(dayOfWeek: DayOfWeek) : String
    {
        return dayOfWeek.toString()
    }
    @TypeConverter
    fun stringToDayOfWeek(dayOfWeek: String) : DayOfWeek
    {
        return DayOfWeek.valueOf(dayOfWeek)
    }
}