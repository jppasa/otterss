package dev.jpvillegas.otterss.navigation

import dev.jpvillegas.otterss.R

sealed class NavigationItem(var titleRes: Int, var iconRes: Int, var screen_route: String) {
    object Home : NavigationItem(R.string.home, R.drawable.ic_round_home_24, "home")
    object Search : NavigationItem(R.string.feeds, R.drawable.ic_round_search_24, "search")
    //object Lists : NavigationItem(R.string.lists, R.drawable.ic_round_play_list_24, "lists")
    object Settings : NavigationItem(R.string.settings, R.drawable.ic_round_settings_24, "settings")

    companion object {
        fun listed() = listOf(
            Home, Search, Settings
        )
    }
}