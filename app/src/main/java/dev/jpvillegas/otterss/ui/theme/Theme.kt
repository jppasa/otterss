package dev.jpvillegas.otterss.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.jpvillegas.otterss.settings.ColorThemePref

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val BrownColorPalette = lightColors(
    primary = Brown,
    primaryVariant = LightBrown,
    secondary = LightBlue100,
    background = LightBeige,
    surface = Beige,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LightBlue700,
    onSurface = LightBlue600,
)

private val LightColorPalette = lightColors(
    primary = GreyPrimary,
    primaryVariant = DarkGreyPrimary,
    secondary = OrangeAccent,
    background = LightGreyPrimary,
    surface = White,
    onPrimary = GreyTextPrimary,
    onSecondary = White,
    onBackground = Black,
    onSurface = Black,
)



//private val EPaperWhiteColorPalette = lightColors(
//    primary = ,
//    primaryVariant = Purple700,
//    secondary = Teal200
//
//    /* Other default colors to override
//    background = Color.White,
//    surface = Color.White,
//    onPrimary = Color.White,
//    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black,
//    */
//)

@Composable
fun OtteRssTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    currentTheme: ColorThemePref = ColorThemePref.LIGHT,
    content: @Composable () -> Unit
) {
    val colors = when(currentTheme) {
        ColorThemePref.DARK -> DarkColorPalette
        ColorThemePref.LIGHT -> LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}