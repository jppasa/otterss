package dev.jpvillegas.otterss.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.jpvillegas.otterss.R
import dev.jpvillegas.otterss.ui.theme.OtteRssTheme


@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(LocalContext.current)
    )

    val items by viewModel.itemsFlow.collectAsState(initial = emptyList())

    if (items.isEmpty()) {
        EmptyListHome()
    } else {
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            items.forEach {
                item {
                    Text(
                        text = it.title ?: "No title",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onBackground,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }
            }
        }
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