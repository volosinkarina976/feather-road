package com.BzNpXqL.vTmJrK.domain.usecase

import com.BzNpXqL.vTmJrK.domain.model.Board
import com.BzNpXqL.vTmJrK.domain.model.BuiltScheme
import com.BzNpXqL.vTmJrK.domain.model.Cell
import com.BzNpXqL.vTmJrK.domain.model.Edge
import com.BzNpXqL.vTmJrK.domain.model.RouteColor
import com.BzNpXqL.vTmJrK.domain.model.Scheme
import com.BzNpXqL.vTmJrK.domain.model.SignShape
import kotlin.random.Random

class BuildSchemeUseCase {

    private data class Layout(
        val cells: List<Cell>,
        val routes: List<RouteColor>
    )

    operator fun invoke(scheme: Scheme, attempt: Int): BuiltScheme {
        val layout = buildLayout(scheme)
        val random = Random(scheme.seed * 31 + attempt * 7919 + 13)
        val scrambled = layout.cells.toMutableList()
        val turns = MutableList(layout.cells.size) { 0 }
        var minMoves = 0
        for (index in layout.cells.indices) {
            val cell = layout.cells[index]
            if (cell is Cell.Sign) {
                val limit = cell.shape.orientations
                val offset = 1 + random.nextInt(limit - 1)
                val rotated = (cell.orientation + offset) % limit
                scrambled[index] = cell.copy(orientation = rotated)
                turns[index] = rotated
                minMoves += limit - offset
            }
        }
        return BuiltScheme(
            board = Board(scheme.cols, scheme.rows, scrambled),
            turns = turns,
            routes = layout.routes,
            minMoves = minMoves,
            moveLimit = minMoves + scheme.slack
        )
    }

    private fun buildLayout(scheme: Scheme): Layout {
        for (attempt in 0 until 24) {
            val candidate = tryLayout(scheme, Random(scheme.seed + attempt * 101 + 7))
            if (candidate != null) return candidate
        }
        return fallbackLayout(scheme)
    }

    private fun tryLayout(scheme: Scheme, random: Random): Layout? {
        val total = scheme.cols * scheme.rows
        val used = BooleanArray(total)
        val cells = MutableList<Cell>(total) { Cell.Grass }
        val routes = mutableListOf<RouteColor>()
        val palette = RouteColor.entries
        for (pair in 0 until scheme.pairs) {
            val path = findPath(scheme, used, random) ?: return null
            val route = palette[pair % palette.size]
            routes.add(route)
            applyPath(scheme, path, route, cells)
            for (index in path) used[index] = true
        }
        return Layout(cells, routes)
    }

    private fun findPath(scheme: Scheme, used: BooleanArray, random: Random): List<Int>? {
        val window = ((scheme.cols + 1)..(scheme.cols * 2)).toMutableList()
        window.shuffle(random)
        val shorter = (3..scheme.cols).toMutableList()
        shorter.shuffle(random)
        val lengths = window + shorter
        val starts = (0 until scheme.cols * scheme.rows).filter { !used[it] }.toMutableList()
        starts.shuffle(random)
        val probes = starts.take(10)
        for (length in lengths) {
            for (start in probes) {
                val path = walk(scheme, used, start, length, random)
                if (path != null) return path
            }
        }
        return null
    }

    private fun walk(
        scheme: Scheme,
        used: BooleanArray,
        start: Int,
        length: Int,
        random: Random
    ): List<Int>? {
        if (length < 3 || used[start]) return null
        val path = mutableListOf(start)
        val onPath = BooleanArray(used.size)
        onPath[start] = true
        var budget = 3000

        fun advance(): Boolean {
            if (path.size == length) return true
            if (budget <= 0) return false
            budget -= 1
            val current = path[path.size - 1]
            val col = current % scheme.cols
            val row = current / scheme.cols
            val directions = Edge.entries.toMutableList()
            directions.shuffle(random)
            for (direction in directions) {
                val nextCol = col + direction.dx
                val nextRow = row + direction.dy
                if (nextCol < 0 || nextRow < 0) continue
                if (nextCol >= scheme.cols || nextRow >= scheme.rows) continue
                val nextIndex = nextRow * scheme.cols + nextCol
                if (used[nextIndex] || onPath[nextIndex]) continue
                path.add(nextIndex)
                onPath[nextIndex] = true
                if (advance()) return true
                onPath[nextIndex] = false
                path.removeAt(path.size - 1)
            }
            return false
        }

        return if (advance()) path.toList() else null
    }

    private fun applyPath(
        scheme: Scheme,
        path: List<Int>,
        route: RouteColor,
        cells: MutableList<Cell>
    ) {
        val first = path[0]
        val last = path[path.size - 1]
        cells[first] = Cell.Source(route, direction(scheme, first, path[1]))
        cells[last] = Cell.Target(route, direction(scheme, last, path[path.size - 2]))
        for (step in 1 until path.size - 1) {
            val index = path[step]
            val incoming = direction(scheme, index, path[step - 1])
            val outgoing = direction(scheme, index, path[step + 1])
            val shape = SignShape.forEdges(incoming, outgoing)
            cells[index] = Cell.Sign(shape, SignShape.orientationFor(shape, incoming, outgoing))
        }
    }

    private fun direction(scheme: Scheme, from: Int, to: Int): Edge {
        val dx = (to % scheme.cols) - (from % scheme.cols)
        val dy = (to / scheme.cols) - (from / scheme.cols)
        return Edge.entries.first { it.dx == dx && it.dy == dy }
    }

    private fun fallbackLayout(scheme: Scheme): Layout {
        val total = scheme.cols * scheme.rows
        val cells = MutableList<Cell>(total) { Cell.Grass }
        val routes = mutableListOf<RouteColor>()
        val palette = RouteColor.entries
        val lanes = minOf(scheme.pairs, scheme.rows)
        for (lane in 0 until lanes) {
            val route = palette[lane % palette.size]
            routes.add(route)
            applyPath(scheme, (0 until scheme.cols).map { lane * scheme.cols + it }, route, cells)
        }
        return Layout(cells, routes)
    }
}
