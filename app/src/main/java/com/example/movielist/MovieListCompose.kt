package com.example.movielist

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.animelist.R
import com.example.movielist.ui.navigation.NavigateItem
import com.example.movielist.ui.navigation.Screen
import com.example.movielist.ui.screen.about.about
import com.example.movielist.ui.screen.detail.detail
import com.example.movielist.ui.screen.favorite.favorite
import com.example.movielist.ui.screen.home.home

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListCompose(
    navController: NavHostController = rememberNavController()
) {
    val navBackStack by navController.currentBackStackEntryAsState()
    val curentRoute = navBackStack?.destination?.route

    Scaffold(
        bottomBar = {
            if (curentRoute != Screen.DetailMovie.route) {
                BottomBar(navController)
            }
        },
        modifier = Modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                home(
                    navigateToDetail = { movieId ->
                        navController.navigate(Screen.DetailMovie.createRoute(movieId))
                    }
                )
            }
            composable(Screen.Profile.route) {
                about()
            }
            composable(Screen.Favorite.route) {
                favorite(
                    navigateToDetail = { movieId ->
                        navController.navigate(Screen.DetailMovie.createRoute(movieId))
                    }
                )
            }
            composable(
                route = Screen.DetailMovie.route,
                arguments = listOf(
                    navArgument("movieId") {
                        type = NavType.IntType
                    }
                )
            ) {
                val id = it.arguments?.getInt("movieId") ?: -1
                detail(
                    movieId = id,
                    backNavigation = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        modifier = modifier
    ) {
        val navBackStack by navController.currentBackStackEntryAsState()
        val curentRoute = navBackStack?.destination?.route
        val navigateItem = listOf(
            NavigateItem(
                icon = Icons.Rounded.FavoriteBorder,
                title = stringResource(R.string.fav),
                screen = Screen.Favorite
            ),
            NavigateItem(
                icon = Icons.Default.AccountCircle,
                title = stringResource(R.string.menu_profile),
                screen = Screen.Profile
            ),
            NavigateItem(
                icon = Icons.Default.Home,
                title = stringResource(R.string.menu_home),
                screen = Screen.Home
            )
        )
        BottomNavigation {
            navigateItem.map { navItem ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = navItem.icon,
                            contentDescription = navItem.title
                        )
                    },
                    label = {
                        Text(navItem.title)
                    },
                    onClick = {
                        navController.navigate(navItem.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    selected = curentRoute == navItem.screen.route
                )
            }
        }
    }
}