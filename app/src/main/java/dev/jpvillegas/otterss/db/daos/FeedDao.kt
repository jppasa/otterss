package dev.jpvillegas.otterss.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.jpvillegas.otterss.db.entities.FeedEntity

@Dao
interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(feed: FeedEntity) : Long

    @Query("SELECT * FROM FeedEntity WHERE url = :urlStr")
    fun getByUrl(urlStr: String) : FeedEntity?
}