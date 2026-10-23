package com.BzNpXqL.vTmJrK.domain.usecase

import com.BzNpXqL.vTmJrK.domain.model.Board
import com.BzNpXqL.vTmJrK.domain.model.Cell
import com.BzNpXqL.vTmJrK.domain.model.RouteColor
import com.BzNpXqL.vTmJrK.domain.model.RouteTrace
import com.BzNpXqL.vTmJrK.domain.model.TraceResult

class TraceRoutesUseCase {

    operator fun invoke(board: Board, routes: List<RouteColor>): TraceResult {
        if (board.cells.isEmpty() || routes.isEmpty()) {
            return TraceResult(emptyList(), emptyMap(), emptySet(), emptySet(), 0, false)
        }
        val traces = routes.map { trace(board, it) }
        val claims = mutableMapOf<Int, MutableList<RouteColor>>()
        for (item in traces) {
            for (index in item.path) {
                if (board.cellAt(index) is Cell.Sign) {
                    claims.getOrPut(index) { mutableListOf() }.add(item.route)
                }
            }
        }
        val crossed = claims.filterValues { it.size > 1 }.keys.toSet()
        val ownership = mutableMapOf<Int, RouteColor>()
        for (entry in claims) {
            if (entry.value.size == 1) ownership[entry.key] = entry.value[0]
        }
        val completedCells = mutableSetOf<Int>()
        var completed = 0
        for (item in traces) {
            val clean = item.complete && item.path.none { crossed.contains(it) }
            if (clean) {
                completed += 1
                completedCells.addAll(item.path)
            }
        }
        val solved = completed == routes.size && crossed.isEmpty()
        return TraceResult(traces, ownership, crossed, completedCells, completed, solved)
    }

    private fun trace(board: Board, route: RouteColor): RouteTrace {
        val startIndex = board.cells.indexOfFirst { it is Cell.Source && it.route == route }
        if (startIndex < 0) return RouteTrace(route, emptyList(), false)
        val source = board.cells[startIndex] as Cell.Source
        val path = mutableListOf(startIndex)
        var heading = source.edge
        var current = startIndex
        var guard = board.cells.size + 2
        while (guard > 0) {
            guard -= 1
            val col = current % board.cols + heading.dx
            val row = current / board.cols + heading.dy
            if (col < 0 || row < 0 || col >= board.cols || row >= board.rows) {
                return RouteTrace(route, path, false)
            }
            val next = row * board.cols + col
            when (val cell = board.cells[next]) {
                is Cell.Grass -> return RouteTrace(route, path, false)
                is Cell.Source -> return RouteTrace(route, path, false)
                is Cell.Target -> {
                    if (cell.route != route || cell.edge != heading.opposite) {
                        return RouteTrace(route, path, false)
                    }
                    path.add(next)
                    return RouteTrace(route, path, true)
                }
                is Cell.Sign -> {
                    val edges = cell.shape.edges(cell.orientation)
                    if (!edges.contains(heading.opposite)) return RouteTrace(route, path, false)
                    if (path.contains(next)) return RouteTrace(route, path, false)
                    path.add(next)
                    heading = edges.first { it != heading.opposite }
                    current = next
                }
            }
        }
        return RouteTrace(route, path, false)
    }
}
