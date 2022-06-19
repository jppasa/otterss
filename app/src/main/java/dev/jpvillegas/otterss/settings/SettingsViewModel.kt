package dev.jpvillegas.otterss.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    val colorThemeFlow: Flow<ColorThemePref> = dataStore.data.map { preferences ->
        val value = preferences[COLOR_THEME_PREF] ?: ColorThemePref.LIGHT.name
        ColorThemePref.valueOf(value)
    }

    fun selectColorTheme(colorThemePref: ColorThemePref) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[COLOR_THEME_PREF] = colorThemePref.name
            }
        }
    }

    companion object {
        private val COLOR_THEME_PREF = stringPreferencesKey("color_theme")
    }
}

enum class ColorThemePref {
    LIGHT,
    DARK
}