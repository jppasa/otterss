package dev.jpvillegas.otterss.feeds

import android.content.Context
import androidx.lifecycle.*
import dev.jpvillegas.otterss.db.AppDb
import dev.jpvillegas.otterss.db.FeedDbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserException
import tw.ktrssreader.Reader
import tw.ktrssreader.kotlin.model.channel.AutoMixChannelData

class FeedsViewModel(
    private val feedDbRepository: FeedDbRepository
) : ViewModel() {

    data class FeedUiState(
        val searchUrl: String? = null,
        val searchingProgress: Boolean = false,
        val feed: AutoMixChannelData? = null,
        val subscribed: Boolean = false,
        val invalidFeed: Boolean = false,
        val error: Boolean = false,
        val errorMsg: String? = null
    )

    private val _feedUiState = MutableLiveData(FeedUiState())

    val feedUiState: LiveData<FeedUiState>
        get() = _feedUiState

    fun searchFeed(urlStr: String) {
        _feedUiState.value = _feedUiState.value?.copy(
            searchUrl = urlStr,
            searchingProgress = true
        )

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val result = Reader.coRead<AutoMixChannelData>(url = urlStr)
                    FetchFeedResult.Success(result)
                } catch (e: XmlPullParserException) {
                    FetchFeedResult.XmlError(e.message)
                } catch (e: Exception) {
                    FetchFeedResult.Error(e.message)
                }
            }

            val subscribed = feedDbRepository.isSubscribed(urlStr)

            when (result) {
                is FetchFeedResult.Success -> {
                    _feedUiState.value = _feedUiState.value?.copy(
                        feed = result.feed,
                        subscribed = subscribed,
                        searchingProgress = false,
                        invalidFeed = false,
                        error = false
                    )
                }
                is FetchFeedResult.XmlError -> {
                    _feedUiState.value = _feedUiState.value?.copy(
                        invalidFeed = true,
                        searchingProgress = false,
                        error = false
                    )
                }
                is FetchFeedResult.Error -> {
                    _feedUiState.value = _feedUiState.value?.copy(
                        error = true,
                        errorMsg = result.errorMsg,
                        searchingProgress = false
                    )
                }
            }
        }
    }

    fun subscribeToFeed(urlStr: String, feed: AutoMixChannelData) {
        viewModelScope.launch {
            val id = feedDbRepository.insertAutoMixChannelDataAsFeed(urlStr, feed)

            if (id > 0) {
                _feedUiState.value = _feedUiState.value?.copy(subscribed = true)
            }
        }
    }

    abstract class FetchFeedResult {
        class Success(val feed: AutoMixChannelData) : FetchFeedResult()
        class XmlError(val errorMsg: String?) : FetchFeedResult()
        class Error(val errorMsg: String?) : FetchFeedResult()
    }
}

class FeedsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    private val feedDbRepository by lazy {
        val db = AppDb.getInstance(context)
        FeedDbRepository(db.feedDao(), db.feedItemDao())
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FeedsViewModel(feedDbRepository) as T
    }
}