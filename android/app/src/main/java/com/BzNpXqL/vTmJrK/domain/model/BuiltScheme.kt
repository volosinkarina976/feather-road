package com.BzNpXqL.vTmJrK.domain.model

data class BuiltScheme(
    val board: Board,
    val turns: List<Int>,
    val routes: List<RouteColor>,
    val minMoves: Int,
    val moveLimit: Int
)
