package dev.jpvillegas.otterss.home

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.jpvillegas.otterss.R
import dev.jpvillegas.otterss.ui.theme.OtteRssTheme
import dev.jpvillegas.otterss.util.DateUtils.contextualDate
import dev.jpvillegas.otterss.util.DateUtils.parseDate
import tw.ktrssreader.kotlin.model.item.AutoMixItem
import tw.ktrssreader.kotlin.model.item.AutoMixItemData
import tw.ktrssreader.kotlin.model.item.Category


@Composable
fun HomeScreen(
    loading: Boolean,
    feedItems: List<AutoMixItem>,
    onFeedsClicked: () -> Unit
) {
    if (loading) {
        LoadingHome()
    }

    AnimatedVisibility(
        visible = !loading && feedItems.isEmpty(),
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {
        EmptyListHome(onFeedsClicked)
    }

    AnimatedVisibility(
        visible = !loading && feedItems.isNotEmpty(),
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {
        val context = LocalContext.current
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(id = R.string.articles),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            items(feedItems) {
                FeedItem(it, onItemClick = { item ->
                    Log.d("ITEMCLICK", "$item")
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.link)))
                })
            }
        }
    }
}

@Composable
fun FeedItem(
    item: AutoMixItem,
    onItemClick: (AutoMixItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(item) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            item.pubDate.parseDate()?.let {
                Text(
                    text = contextualDate(
                        date = it,
                        todayStr = stringResource(id = R.string.today),
                        yesterdayStr = stringResource(id = R.string.yesterday),
                        daysAgoStr = stringResource(id = R.string.days_ago),
                    ),
                    fontSize = 12.sp,
                    modifier = Modifier.align(alignment = Alignment.End)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = item.title ?: item.simpleTitle ?: "n/a",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.Start,
                fontSize = 18.sp
            )

            item.description?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colors.onBackground,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            item.categories?.mapNotNull { it.name }?.let {
                if (it.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Label(text = it.first())
                        Spacer(modifier = Modifier.width(4.dp))
                        if (it.size > 1) {
                            Label(text = "+ ${it.size - 1}")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Label(text: String) {
    val backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.5f)

    Chip(
        onClick = {},
        colors = ChipDefaults.chipColors(
            backgroundColor = backgroundColor,
            contentColor = MaterialTheme.colors.onSecondary
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun LoadingHome() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.loading),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    }
}

@Composable
fun EmptyListHome(
    onFeedsClicked: () -> Unit
) {
    val openUrlDialog = remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Text(
            text = stringResource(id = R.string.no_items),
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        Spacer(Modifier.size(16.dp))

        Button(
            onClick = onFeedsClicked
        ) {
            Text(
                text = stringResource(id = R.string.search_feeds),
            )
        }
    }

    if (openUrlDialog.value) {
        Dialog(
            onDismissRequest = { openUrlDialog.value = false }
        ) {
            AddFeedDialogContent(
                onAddButtonPressed = {

                },
                onDismiss = {
                    openUrlDialog.value = false
                }
            )
        }
    }
}

@Composable
fun AddFeedDialogContent(
    onDismiss: (() -> Unit)? = null,
    onAddButtonPressed: (() -> Unit)? = null
) {
    val text = remember { mutableStateOf("") }
    val searching = remember { mutableStateOf(false) }

    if (searching.value) {

    }
    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Add your url here")
            TextField(
                value = text.value,
                onValueChange = {
                    text.value = it
                },
            )
            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    Log.d("TEXT", text.value)
                }) {
                Text(text = "Search")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeLoadingPreview() {
    OtteRssTheme {
        HomeScreen(
            loading = true,
            feedItems = emptyList()
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    OtteRssTheme {
        HomeScreen(
            loading = false,
            feedItems = listOf(
                autoMixItemData(
                    title = "Title 1",
                    description = "Item description",
                    pubDate = "Sun, 18 Jun 2022 17:30:15"
                ),
                autoMixItemData(
                    title = "Title 2",
                    description = "Item description",
                    pubDate = "Sun, 17 Jun 2022 17:30:15"
                ),
                autoMixItemData(
                    title = "Title 3",
                    description = "Item description",
                    pubDate = "Sun, 14 Jun 2022 17:30:15"
                ),
                autoMixItemData(
                    title = "Title 4",
                    description = "Item description",
                    pubDate = "Sun, 01 Jun 2022 17:30:15"
                ),
            )
        ) {}
    }
}

fun autoMixItemData(
    title: String,
    description: String,
    pubDate: String
): AutoMixItemData {
    return AutoMixItemData(
        title = title,
        enclosure = null,
        guid = null,
        pubDate = pubDate,
        description = description,
        link = null,
        author = null,
        categories = listOf(
            Category("Android!", "developer.android.com"),
            Category("Android 2!", "developer.android.com"),
        ),
        comments = null,
        source = null,
        simpleTitle = null,
        duration = null,
        image = null,
        explicit = null,
        episode = null,
        season = null,
        episodeType = null,
        block = null,
        summary = null,
        subtitle = null,
        keywords = null,
    )
}