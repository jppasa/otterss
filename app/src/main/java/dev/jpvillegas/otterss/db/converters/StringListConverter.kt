package dev.jpvillegas.otterss.db.converters

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class StringListConverter {
    @TypeConverter
    fun stringToStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun stringListToString(list: List<String>): String {
        return Json.encodeToString(list)
    }
}