package dev.jpvillegas.otterss.feeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserException
import tw.ktrssreader.Reader
import tw.ktrssreader.kotlin.model.channel.AutoMixChannelData

class FeedsViewModel : ViewModel() {

    data class FeedUiState(
        val searchingProgress: Boolean = false,
        val feed: AutoMixChannelData? = null,
        val invalidFeed: Boolean = false,
        val error: Boolean = false,
        val errorMsg: String? = null
    )

    private val _feedUiState = MutableLiveData(FeedUiState())

    val feedUiState: LiveData<FeedUiState>
        get() = _feedUiState

    fun searchFeed(urlStr: String) {
        _feedUiState.value = _feedUiState.value?.copy(searchingProgress = true)

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

            when (result) {
                is FetchFeedResult.Success -> {
                    _feedUiState.value = _feedUiState.value?.copy(
                        feed = result.feed,
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

    abstract class FetchFeedResult {
        class Success(val feed: AutoMixChannelData) : FetchFeedResult()
        class XmlError(val errorMsg: String?) : FetchFeedResult()
        class Error(val errorMsg: String?) : FetchFeedResult()
    }
}