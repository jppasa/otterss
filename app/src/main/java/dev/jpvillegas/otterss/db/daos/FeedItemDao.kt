package dev.jpvillegas.otterss.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import dev.jpvillegas.otterss.db.entities.FeedItemEntity

@Dao
interface FeedItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<FeedItemEntity>) : List<Long>
}