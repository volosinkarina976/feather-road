package com.BzNpXqL.vTmJrK.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object Game : Screen
    data class GameOver(val win: Boolean) : Screen
}
