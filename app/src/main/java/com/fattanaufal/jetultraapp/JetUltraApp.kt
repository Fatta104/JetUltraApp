package com.fattanaufal.jetultraapp

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fattanaufal.jetultraapp.navigation.NavigationItem
import com.fattanaufal.jetultraapp.navigation.Screen
import com.fattanaufal.jetultraapp.ui.screen.about.AboutScreen
import com.fattanaufal.jetultraapp.ui.screen.detail.DetailScreen
import com.fattanaufal.jetultraapp.ui.screen.favorite.FavoriteScreen
import com.fattanaufal.jetultraapp.ui.screen.home.HomeScreen

@Composable
fun JetUltraApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (currentRoute != Screen.DetailUltra.route) {
                if (currentRoute != Screen.Favorite.route) {
                    if (currentRoute != Screen.About.route) {
                        BottomBar(navController = navController)
                    }
                }
            }
        }
    ) {innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(
                route = Screen.Home.route
            ){
                HomeScreen(navigateToDetail = { ultraId ->
                    navController.navigate(Screen.DetailUltra.createRoute(ultraId))
                })
            }
            composable(
                route = Screen.DetailUltra.route,
                arguments = listOf(navArgument("ultraId") { type = NavType.StringType})
            ){
                val id = it.arguments?.getString("ultraId") ?: ""
                Log.d( "test", id)
                DetailScreen(
                    ultraId = id,
                    navigateBack = { navController.navigateUp()},
                    )
                }
            composable(
                route = Screen.Favorite.route
            ){
                FavoriteScreen(
                    navigateBack = {navController.navigateUp()},
                    navigateToDetail = {ultraId ->
                        navController.navigate(Screen.DetailUltra.createRoute(ultraId))
                    }
                )
            }
            composable(
                route = Screen.About.route
            ){
                AboutScreen(
                    navigateBack = {navController.navigateUp() }
                )

                }
            }
        }
    }

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavigationBar(
        modifier = modifier,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title =  stringResource(R.string.menu_favorite),
                icon = Icons.Default.Favorite,
                screen = Screen.Favorite
            ),
            NavigationItem(
                title = stringResource(R.string.menu_about),
                icon = Icons.Default.Person,
                screen = Screen.About
            ),
        )
        navigationItems.map { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title)},
                selected =currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route){
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }

    }
}