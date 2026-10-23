package com.BzNpXqL.vTmJrK.domain.model

data class RouteTrace(
    val route: RouteColor,
    val path: List<Int>,
    val complete: Boolean
)

data class TraceResult(
    val traces: List<RouteTrace>,
    val ownership: Map<Int, RouteColor>,
    val crossed: Set<Int>,
    val completedCells: Set<Int>,
    val completed: Int,
    val solved: Boolean
)
