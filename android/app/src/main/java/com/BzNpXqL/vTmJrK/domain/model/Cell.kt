package com.BzNpXqL.vTmJrK.domain.model

sealed interface Cell {
    data object Grass : Cell
    data class Source(val route: RouteColor, val edge: Edge) : Cell
    data class Target(val route: RouteColor, val edge: Edge) : Cell
    data class Sign(val shape: SignShape, val orientation: Int) : Cell
}
