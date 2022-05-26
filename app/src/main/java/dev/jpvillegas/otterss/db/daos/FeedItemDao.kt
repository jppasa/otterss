package dev.jpvillegas.otterss.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.jpvillegas.otterss.db.entities.FeedItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<FeedItemEntity>) : List<Long>

    @Query("SELECT * FROM FeedItemEntity")
    fun allAsFlow(): Flow<List<FeedItemEntity>>
}