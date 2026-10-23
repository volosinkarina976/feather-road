package com.BzNpXqL.vTmJrK.presentation.game

import com.BzNpXqL.vTmJrK.core.config.GameConfig
import com.BzNpXqL.vTmJrK.domain.model.Board
import com.BzNpXqL.vTmJrK.domain.model.RouteColor

enum class Phase {
    Idle,
    Routing,
    Checking,
    Solved,
    Failed
}

enum class HintKind {
    Start,
    Routing,
    Ready,
    Crossing
}

data class GameUiState(
    val phase: Phase = Phase.Idle,
    val board: Board = Board.EMPTY,
    val turns: List<Int> = emptyList(),
    val routes: List<RouteColor> = emptyList(),
    val movesLeft: Int = 0,
    val minMoves: Int = 0,
    val movesUsed: Int = 0,
    val checksLeft: Int = GameConfig.CHECK_LIMIT,
    val routesDone: Int = 0,
    val routesTotal: Int = 0,
    val ownership: Map<Int, RouteColor> = emptyMap(),
    val completedCells: Set<Int> = emptySet(),
    val completedRoutes: Set<RouteColor> = emptySet(),
    val crossedCells: Set<Int> = emptySet(),
    val flashingCells: Set<Int> = emptySet(),
    val schemeIndex: Int = 0,
    val schemeCount: Int = GameConfig.SCHEME_COUNT,
    val hint: HintKind = HintKind.Start
)
