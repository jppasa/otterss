package dev.jpvillegas.otterss.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
class FeedEntity(
    @PrimaryKey
    val url: String,
    val title: String?,
    val description: String?,
    @Embedded
    val image: FeedImage?,
    val language: String?,
    val categories: List<FeedCategory>?,
    val link: String?,
    val copyright: String?,
    val managingEditor: String?,
    val webMaster: String?,
    val pubDate: String?,
    val lastBuildDate: String?,
    val generator: String?,
    val docs: String?,
    @Embedded
    val cloud: FeedCloud?,
    val ttl: Int?,
    val rating: String?,
    @Embedded
    val textInput: FeedTextInput?,
    val skipHours: List<Int>?,
    val skipDays: List<String>?,
//    val items: List<FeedItemEntity>?,
    val simpleTitle: String?,
    val explicit: Boolean?,
    val email: String?,
    val author: String?,
    @Embedded
    val owner: FeedOwner?,
    val type: String?,
    val newFeedUrl: String?,
    val block: Boolean?,
    val complete: Boolean?,
)

class FeedImage(
    @ColumnInfo(name = "image-link")
    val link: String?,
    @ColumnInfo(name = "image-title")
    val title: String?,
    @ColumnInfo(name = "image-url")
    val url: String?,
    @ColumnInfo(name = "image-description")
    val description: String?,
    @ColumnInfo(name = "image-height")
    val height: Int?,
    @ColumnInfo(name = "image-width")
    val width: Int?,
)

@Serializable
class FeedCategory(
    val name: String?,
    val domain: String?,
)

class FeedCloud(
    @ColumnInfo(name = "cloud-domain")
    val domain: String?,
    @ColumnInfo(name = "cloud-port")
    val port: Int?,
    @ColumnInfo(name = "cloud-path")
    val path: String?,
    @ColumnInfo(name = "cloud-registerProcedure")
    val registerProcedure: String?,
    @ColumnInfo(name = "cloud-protocol")
    val protocol: String?,
)

class FeedTextInput(
    @ColumnInfo(name = "textInput-title")
    val title: String?,
    @ColumnInfo(name = "textInput-description")
    val description: String?,
    @ColumnInfo(name = "textInput-name")
    val name: String?,
    @ColumnInfo(name = "textInput-link")
    val link: String?,
)

class FeedOwner(
    @ColumnInfo(name = "owner-name")
    val name: String?,
    @ColumnInfo(name = "owner-email")
    val email: String?,
)