package dev.jpvillegas.otterss.db.converters

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class StringListConverter {
    @TypeConverter
    fun stringToStringList(value: String?): List<String>? {
        if (value == null) return null
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun stringListToString(list: List<String>?): String? {
        if (list == null) return null
        return Json.encodeToString(list)
    }
}