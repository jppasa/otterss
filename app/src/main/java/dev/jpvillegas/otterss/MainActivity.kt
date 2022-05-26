package dev.jpvillegas.otterss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.jpvillegas.otterss.feeds.FeedsScreen
import dev.jpvillegas.otterss.home.HomeScreen
import dev.jpvillegas.otterss.navigation.NavigationItem
import dev.jpvillegas.otterss.ui.theme.OtteRssTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OtteRssTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreenView(listOf())
                }
            }
        }
    }
}

@Composable
fun MainScreenView(items: List<String>) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) {
        NavigationGraph(navController = navController, items)
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    items: List<String>,
) {
    NavHost(navController, startDestination = NavigationItem.Home.screen_route) {
        composable(NavigationItem.Home.screen_route) {
            HomeScreen()
        }
        composable(NavigationItem.Feeds.screen_route) {
            FeedsScreen()
        }
        composable(NavigationItem.Lists.screen_route) {
            HomeScreen()
        }
        composable(NavigationItem.Settings.screen_route) {
            HomeScreen()
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = NavigationItem.listed()

    Card(
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        modifier = Modifier.padding(top = 8.dp)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OtteRssTheme {
        MainScreenView(
            listOf(
//            "Feed 1",
//            "Feed 2",
//            "Feed 3",
//            "Feed 4",
            )
        )
    }
}