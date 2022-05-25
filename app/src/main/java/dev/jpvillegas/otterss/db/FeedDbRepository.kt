package dev.jpvillegas.otterss.db

import dev.jpvillegas.otterss.db.daos.FeedDao
import dev.jpvillegas.otterss.db.daos.FeedItemDao
import dev.jpvillegas.otterss.db.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tw.ktrssreader.kotlin.model.channel.*
import tw.ktrssreader.kotlin.model.item.Category

class FeedDbRepository(
    private val feedDao: FeedDao,
    private val feedItemDao: FeedItemDao,
) {

    suspend fun insertAutoMixChannelDataAsFeed(
        url: String,
        channel: AutoMixChannelData
    ) = withContext(Dispatchers.IO) {
        feedDao.insert(channel.toFeedEntity(url))
    }

    suspend fun isSubscribed(url: String): Boolean = withContext(Dispatchers.IO) {
        feedDao.getByUrl(url) != null
    }
}

private fun AutoMixChannelData.toFeedEntity(url: String): FeedEntity {
    return FeedEntity(
        url = url,
        title = title,
        description = description,
        image = image.toFeedImage(),
        language = language,
        categories = categories.toFeedCategoryList(),
        link = link,
        copyright = copyright,
        managingEditor = managingEditor,
        webMaster = webMaster,
        pubDate = pubDate,
        lastBuildDate = lastBuildDate,
        generator = generator,
        docs = docs,
        cloud = cloud.toFeedCloud(),
        ttl = ttl,
        rating = rating,
        textInput = textInput.toFeedTextInput(),
        skipHours = skipHours,
        skipDays = skipDays,
        //items= ,
        simpleTitle = simpleTitle,
        explicit = explicit,
        email = email,
        author = author,
        owner = owner.toFeedOwner(),
        type = type,
        newFeedUrl = newFeedUrl,
        block = block,
        complete = complete,
    )
}

private fun Owner?.toFeedOwner(): FeedOwner? {
    if (this == null) return null

    return FeedOwner(
        name = name,
        email = email,
    )
}

private fun TextInput?.toFeedTextInput(): FeedTextInput? {
    if (this == null) return null

    return FeedTextInput(
        title = title,
        description = description,
        name = name,
        link = link,
    )
}

private fun Cloud?.toFeedCloud(): FeedCloud? {
    if (this == null) return null

    return FeedCloud(
        domain = domain,
        port = port,
        path = path,
        registerProcedure = registerProcedure,
        protocol = protocol,
    )
}

private fun List<Category>?.toFeedCategoryList(): List<FeedCategory>? {
    return this?.map { it.toFeedCategory() }
}

private fun Category.toFeedCategory(): FeedCategory {
    return FeedCategory(
        name = name,
        domain = domain,
    )
}

private fun Image?.toFeedImage(): FeedImage? {
    if (this == null) return null

    return FeedImage(
        link = link,
        title = title,
        url = url,
        description = description,
        height = height,
        width = width,
    )
}
