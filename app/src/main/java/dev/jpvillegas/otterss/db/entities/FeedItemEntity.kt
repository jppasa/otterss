package dev.jpvillegas.otterss.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
class FeedItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val feedId: Long,
    val simpleTitle: String?,
    val duration: String?,
    val image: String?,
    val explicit: Boolean?,
    val episode: Int?,
    val season: Int?,
    val episodeType: String?,
    val block: Boolean?,
)