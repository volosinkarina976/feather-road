package com.BzNpXqL.vTmJrK.domain.model

data class RoundResult(
    val schemeIndex: Int = 0,
    val win: Boolean = false,
    val score: Int = 0,
    val stars: Int = 0,
    val movesUsed: Int = 0,
    val movesLeft: Int = 0,
    val routesDone: Int = 0,
    val routesTotal: Int = 0,
    val newBest: Boolean = false
)
