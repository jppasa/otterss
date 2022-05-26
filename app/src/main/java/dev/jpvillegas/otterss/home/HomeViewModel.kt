package dev.jpvillegas.otterss.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.jpvillegas.otterss.db.AppDb
import dev.jpvillegas.otterss.db.FeedDbRepository
import dev.jpvillegas.otterss.db.entities.FeedEntity
import dev.jpvillegas.otterss.db.entities.FeedItemEntity
import dev.jpvillegas.otterss.feeds.FeedsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tw.ktrssreader.Reader
import tw.ktrssreader.kotlin.model.channel.AutoMixChannelData
import tw.ktrssreader.kotlin.model.item.AutoMixItem

class HomeViewModel(
    private val feedDbRepository: FeedDbRepository
) : ViewModel() {

    val itemsInDbFlow = feedDbRepository.itemsAsFlow()

    val itemsFlow = feedDbRepository.feedsAsFlow()
        .flowOn(Dispatchers.IO)
        .map { list ->
            list.mapNotNull {
                Reader.coRead<AutoMixChannelData>(it.url).items
            }.flatMap { it.asIterable() }.sortedByDescending { it.pubDate }
        }
        .flowOn(Dispatchers.IO)

}

class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    private val feedDbRepository by lazy {
        val db = AppDb.getInstance(context)
        FeedDbRepository(db.feedDao(), db.feedItemDao())
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(feedDbRepository) as T
    }
}