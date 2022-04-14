package dev.jpvillegas.otterss.db.converters

import androidx.room.TypeConverter
import dev.jpvillegas.otterss.db.entities.FeedCategory
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FeedCategoryConverter {
    @TypeConverter
    fun stringToListOfFeedCategory(value: String): List<FeedCategory> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun feedCategoryListToString(feedCategoryList: List<FeedCategory>): String {
        return Json.encodeToString(feedCategoryList)
    }
}

