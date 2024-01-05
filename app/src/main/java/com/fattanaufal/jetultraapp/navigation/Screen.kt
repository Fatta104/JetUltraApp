package com.fattanaufal.jetultraapp.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("favorite")
    object About : Screen("about")
    object DetailUltra : Screen("home/{ultraId}") {
        fun createRoute(ultraId: String) = "home/$ultraId"
    }
}
