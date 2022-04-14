package dev.jpvillegas.otterss.feeds

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import dev.jpvillegas.otterss.R
import dev.jpvillegas.otterss.ui.theme.OtteRssTheme
import tw.ktrssreader.kotlin.model.channel.AutoMixChannelData

@Composable
fun FeedsScreen() {
    val viewModel: FeedsViewModel = viewModel()
    val uiState by viewModel.feedUiState.observeAsState()

    uiState?.let {
        FeedsContent(
            searchInProgress = it.searchingProgress,
            error = it.error,
            errorMsg = it.errorMsg,
            invalidFeed = it.invalidFeed,
            feed = it.feed,
            onSearchClicked = { text ->
                viewModel.searchFeed(text)
            }
        )
    }
}

@Composable
fun FeedsContent(
    searchInProgress: Boolean,
    error: Boolean,
    errorMsg: String?,
    invalidFeed: Boolean,
    feed: AutoMixChannelData?,
    onSearchClicked: (String) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        SearchBar(onSearchClicked = onSearchClicked)

        AnimatedVisibility(visible = searchInProgress) {
            CircularProgressIndicator()
        }

        when {
            error -> {
                Text(text = stringResource(id = R.string.error_while_fetching_feed))
                Log.e("FETCH_ERROR", errorMsg.toString())
            }
            invalidFeed -> {
                Text(text = stringResource(id = R.string.feed_is_invalid))
            }
        }

        AnimatedVisibility(
            visible = feed != null && !searchInProgress,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut()
        ) {
            FeedItem(feed)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun FeedItem(feed: AutoMixChannelData?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        ConstraintLayout {
            val (imageRef, titleRef, descriptionRef) = createRefs()

            val url = feed?.image?.url
            val title = feed?.title ?: stringResource(id = R.string.no_title)

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .constrainAs(imageRef) {
                        start.linkTo(parent.start, margin = 16.dp)
                        linkTo(
                            top = parent.top,
                            bottom = parent.bottom,
                            topMargin = 16.dp,
                            bottomMargin = 16.dp,
                            bias = 0f
                        )
                    }
            ) {
                if (url != null) {
                    Image(
                        painter = rememberImagePainter(url),
                        contentDescription = stringResource(id = R.string.item_image, title),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colors.secondary,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_round_rss_feed_24),
                            contentDescription = stringResource(id = R.string.item_image, title),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            Text(
                text = title,
                fontSize = 20.sp,
                style = TextStyle.Default.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                ),
                textAlign = TextAlign.Start,
                modifier = Modifier.constrainAs(titleRef) {
                    width = Dimension.fillToConstraints
                    top.linkTo(imageRef.top)
                    start.linkTo(imageRef.end, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
            )

            val description = feed?.description
            if (description != null) {
                Text(
                    text = description,
                    style = TextStyle(
                        color = MaterialTheme.colors.onSurface,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.constrainAs(descriptionRef) {
                        width = Dimension.fillToConstraints
                        start.linkTo(titleRef.start)
                        end.linkTo(titleRef.end)

                        linkTo(
                            top = titleRef.bottom,
                            bottom = parent.bottom,
                            topMargin = 4.dp,
                            bottomMargin = 16.dp,
                            bias = 0f
                        )
                    }
                )
            }


        }
    }
}

@Composable
fun SearchBar(
    onSearchClicked: (String) -> Unit
) {
    val searchText = remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(32.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 32.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = searchText.value,
                label = null,
                onValueChange = {
                    searchText.value = it
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            IconButton(
                onClick = {
                    onSearchClicked(searchText.value)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_search_24),
                    contentDescription = stringResource(id = R.string.search_feeds),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

private val testFeed = AutoMixChannelData(
    title = "Feed title",
    description = "This is the description of the feed",
    image = null,
    language = "en",
    categories = null,
    link = null,
    copyright = null,
    managingEditor = null,
    webMaster = null,
    pubDate = null,
    lastBuildDate = null,
    generator = null,
    docs = null,
    cloud = null,
    ttl = null,
    rating = "4.5",
    textInput = null,
    skipHours = null,
    skipDays = null,
    items = null,
    simpleTitle = null,
    explicit = false,
    email = "me@feed.fed",
    author = "Myself",
    owner = null,
    type = "Feed",
    newFeedUrl = null,
    block = null,
    complete = null,
)

@Preview
@Composable
fun FeedItemPreview() {
    OtteRssTheme {
        FeedItem(testFeed)
    }
}

@Preview
@Composable
fun FeedsScreenPreview() {
    OtteRssTheme {
        FeedsContent(
            searchInProgress = false,
            error = false,
            errorMsg = null,
            invalidFeed = false,
            feed = testFeed,
            onSearchClicked = {}
        )
    }
}