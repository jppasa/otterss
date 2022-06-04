package dev.jpvillegas.otterss.home

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.jpvillegas.otterss.R
import dev.jpvillegas.otterss.ui.theme.OtteRssTheme
import dev.jpvillegas.otterss.util.DateUtils.contextualDate
import dev.jpvillegas.otterss.util.DateUtils.parseDate
import tw.ktrssreader.kotlin.model.item.AutoMixItem


@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(LocalContext.current)
    )

    val loading by viewModel.loading.collectAsState(initial = true)
    val feedItems by viewModel.itemsFlow.collectAsState(initial = emptyList())

    if (loading) {
        LoadingHome()
    }

    AnimatedVisibility(
        visible = !loading && feedItems.isEmpty(),
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {
        EmptyListHome()
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
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            items(feedItems) {
                FeedItem(it, onItemClick = { item ->
                    Log.d("ITEMCLICK", "$item")
                    //context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.link)))
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
            Text(
                text = item.title ?: item.simpleTitle ?: "n/a",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.Start,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            item.pubDate.parseDate()?.let {
                Text(
                    text = contextualDate(
                        date = it,
                        todayStr = stringResource(id = R.string.today),
                        yesterdayStr = stringResource(id = R.string.yesterday),
                        daysAgoStr = stringResource(id = R.string.days_ago),
                    ),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                )
            }

            item.categories?.mapNotNull { it.name }?.let {
                if (it.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
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
            fontFamily = FontFamily.Monospace,
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
fun EmptyListHome() {
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
            onClick = {

            }
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
fun HomePreview() {
    OtteRssTheme {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    OtteRssTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            AddFeedDialogContent()
        }
    }
}