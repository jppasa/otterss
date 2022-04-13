package dev.jpvillegas.otterss.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.jpvillegas.otterss.R
import dev.jpvillegas.otterss.ui.theme.OtteRssTheme


@Composable
fun HomeScreen(items: List<String>) {
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
                        text = it,
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

        Spacer(Modifier.size(32.dp))

        val buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary
        )

        Button(
            shape = RoundedCornerShape(size = 32.dp),
            colors = buttonColors,
            onClick = { openUrlDialog.value = true }
        ) {
            Text(
                text = stringResource(id = R.string.input_url),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Spacer(Modifier.size(16.dp))

        TextButton(
            onClick = {

            }
        ) {
            Text(text = stringResource(id = R.string.browse_feeds))
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
fun AddFeedDialogContent(onDismiss: (() -> Unit)? = null, onAddButtonPressed: (() -> Unit)? = null) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Add your url here")
            TextField(
                value = "", onValueChange = {},
            )
            Button(onClick = {},) {
                Text(text = "Search")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    OtteRssTheme {
        HomeScreen(
            listOf(
//            "Feed 1",
//            "Feed 2",
//            "Feed 3",
//            "Feed 4",
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    OtteRssTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            AddFeedDialogContent()
        }
    }
}