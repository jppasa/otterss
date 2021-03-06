package dev.jpvillegas.otterss

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.jpvillegas.otterss.db.AppDb
import dev.jpvillegas.otterss.db.FeedDbRepository
import dev.jpvillegas.otterss.feeds.FeedsScreen
import dev.jpvillegas.otterss.home.HomeScreen
import dev.jpvillegas.otterss.home.HomeViewModel
import dev.jpvillegas.otterss.navigation.NavigationItem
import dev.jpvillegas.otterss.settings.ColorThemePref
import dev.jpvillegas.otterss.settings.SettingsScreen
import dev.jpvillegas.otterss.settings.SettingsViewModel
import dev.jpvillegas.otterss.ui.theme.OtteRssTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = ViewModelFactory(this)
            )

            val currentTheme = settingsViewModel.colorThemeFlow
                .collectAsState(initial = ColorThemePref.LIGHT)

            OtteRssTheme(
                currentTheme = currentTheme.value
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreenView(currentTheme.value) {
                        settingsViewModel.selectColorTheme(it)
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreenView(
    currentTheme: ColorThemePref,
    onColorThemeSelected: (ColorThemePref) -> Unit
) {
    val factory = ViewModelFactory(LocalContext.current)
    val feedsViewModel: FeedsViewModel = viewModel(factory = factory)
    val homeViewModel: HomeViewModel = viewModel(factory = factory)

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) {

        val homeLoading by homeViewModel.loading.collectAsState(initial = true)
        val homeFeedItems by homeViewModel.itemsFlow.collectAsState(initial = emptyList())

        val feedUiState by feedsViewModel.feedUiState.observeAsState()

        NavHost(
            navController,
            startDestination = NavigationItem.Home.screen_route,
            modifier = Modifier.padding(paddingValues = it)
        ) {
            composable(NavigationItem.Home.screen_route) {
                HomeScreen(homeLoading, homeFeedItems) {
                    navController.navigate(NavigationItem.Search.screen_route)
                }
            }
            composable(NavigationItem.Search.screen_route) {
                FeedsScreen(
                    uiState = feedUiState,
                    onSearchClicked = { text ->
                        feedsViewModel.searchFeed(text)
                    },
                    onSubscribe = { url, feed, fromSearch ->
                        if (url != null) {
                            feedsViewModel.subscribeToFeed(
                                urlStr = url,
                                feed = feed,
                                fromSearch = fromSearch
                            )
                        }
                    }
                )
            }
            composable(NavigationItem.Settings.screen_route) {
                SettingsScreen(currentTheme, onColorThemeSelected)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = NavigationItem.listed()

    Card(
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
    ) {
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { navigationItem ->
                val title = stringResource(id = navigationItem.titleRes)

                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = navigationItem.iconRes),
                            contentDescription = title
                        )
                    },
                    label = { Text(text = title, fontSize = 12.sp) },
                    selectedContentColor = MaterialTheme.colors.onPrimary,
                    unselectedContentColor = MaterialTheme.colors.onPrimary.copy(0.4f),
                    alwaysShowLabel = true,
                    selected = currentRoute == navigationItem.screen_route,
                    onClick = {
                        navController.navigate(navigationItem.screen_route) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

class ViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    private val feedDbRepository by lazy {
        val db = AppDb.getInstance(context)
        FeedDbRepository(db.feedDao(), db.feedItemDao())
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(feedDbRepository) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(context.dataStore) as T
            else -> FeedsViewModel(feedDbRepository) as T
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OtteRssTheme {
        MainScreenView(ColorThemePref.LIGHT) {}
    }
}