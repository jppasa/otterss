package dev.jpvillegas.otterss.settings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jpvillegas.otterss.R
import dev.jpvillegas.otterss.ui.theme.OtteRssTheme

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {

        Text(
            text = stringResource(id = R.string.theme),
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card {
            Column {
                SettingsItem(title = stringResource(id = R.string.theme_light), selected = true)
                Divider()
                SettingsItem(title = stringResource(id = R.string.theme_dark), selected = false)
            }
        }
    }
}

@Composable
fun SettingsItem(title: String, selected: Boolean) {
    Row(
        modifier = Modifier
            .clickable { Log.d("SETTINGS", "Click $title") }
            .padding(16.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )

        if (selected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = title,
                tint = MaterialTheme.colors.secondary
            )
        }
    }
}

@Preview
@Composable
fun SettingsPreview() {
    OtteRssTheme {
        SettingsScreen()
    }
}