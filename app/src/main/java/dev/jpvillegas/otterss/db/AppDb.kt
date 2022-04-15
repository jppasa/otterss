package dev.jpvillegas.otterss.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.jpvillegas.otterss.db.converters.FeedCategoryConverter
import dev.jpvillegas.otterss.db.converters.IntListConverter
import dev.jpvillegas.otterss.db.converters.StringListConverter
import dev.jpvillegas.otterss.db.daos.FeedDao
import dev.jpvillegas.otterss.db.daos.FeedItemDao
import dev.jpvillegas.otterss.db.entities.FeedEntity
import dev.jpvillegas.otterss.db.entities.FeedItemEntity

@Database(
    entities = [
        FeedEntity::class,
        FeedItemEntity::class,
    ],
    version = 1
)
@TypeConverters(
    FeedCategoryConverter::class,
    IntListConverter::class,
    StringListConverter::class,
)
abstract class AppDb : RoomDatabase() {
    abstract fun feedDao(): FeedDao
    abstract fun feedItemDao(): FeedItemDao

    companion object {
        private const val DATABASE_NAME = "otterss_db"
        private var instance: AppDb? = null
        private val initLock = Any()

        fun getInstance(context: Context): AppDb {
            return synchronized(initLock) {
                instance ?: databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    DATABASE_NAME
                ).build().also { instance = it }
            }
        }

    }
}