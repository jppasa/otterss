package dev.jpvillegas.otterss.home

import androidx.lifecycle.ViewModel
import dev.jpvillegas.otterss.db.FeedDbRepository
import dev.jpvillegas.otterss.util.DateUtils.parseDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import tw.ktrssreader.Reader
import tw.ktrssreader.kotlin.model.channel.AutoMixChannelData

class HomeViewModel(
    feedDbRepository: FeedDbRepository
): ViewModel() {

    val loading = MutableStateFlow(true)

    val itemsFlow = feedDbRepository.feedsAsFlow()
        .flowOn(Dispatchers.IO)
        .map { list ->
            val result = list
                .mapNotNull { Reader.coRead<AutoMixChannelData>(it.url).items }
                .flatMap { it.asIterable() }
                .sortedByDescending {
                    it.pubDate.parseDate()
                }

            loading.value = false
            result
        }
        .flowOn(Dispatchers.IO)
}