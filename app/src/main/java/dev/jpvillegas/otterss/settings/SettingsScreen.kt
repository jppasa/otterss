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
fun SettingsScreen(
    colorThemeState: ColorThemePref,
    onColorThemeSelected: (ColorThemePref) -> Unit
) {
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
                SettingsItem(
                    colorThemePref = ColorThemePref.LIGHT,
                    currentTheme = colorThemeState,
                    onColorThemeSelected = onColorThemeSelected
                )
                Divider()
                SettingsItem(
                    colorThemePref = ColorThemePref.DARK,
                    currentTheme = colorThemeState,
                    onColorThemeSelected = onColorThemeSelected
                )
            }
        }
    }
}

@Composable
fun SettingsItem(
    colorThemePref: ColorThemePref,
    currentTheme: ColorThemePref,
    onColorThemeSelected: (ColorThemePref) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onColorThemeSelected(colorThemePref) }
            .padding(16.dp)
    ) {
        val title = stringResource(
            id = when (colorThemePref) {
                ColorThemePref.LIGHT -> R.string.theme_light
                ColorThemePref.DARK -> R.string.theme_dark
            }
        )

        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )

        if (currentTheme == colorThemePref) {
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
fun SettingsPreviewLight() {
    OtteRssTheme {
        SettingsScreen(ColorThemePref.LIGHT) {}
    }
}

@Preview
@Composable
fun SettingsPreviewDark() {
    OtteRssTheme(
        currentTheme = ColorThemePref.DARK
    ) {
        SettingsScreen(ColorThemePref.DARK) {}
    }
}