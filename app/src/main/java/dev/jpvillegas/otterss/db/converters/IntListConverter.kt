package dev.jpvillegas.otterss.db.converters

import androidx.room.TypeConverter

class IntListConverter {
    @TypeConverter
    fun stringToIntList(value: String): List<Int> {
        val list = value.split("\\s*,\\s*".toRegex())
        return list.filter { it.isNotEmpty() }.map { it.toInt() }
    }

    @TypeConverter
    fun intListToString(list: List<Int>): String {
        return list.joinToString(separator = ",")
    }
}