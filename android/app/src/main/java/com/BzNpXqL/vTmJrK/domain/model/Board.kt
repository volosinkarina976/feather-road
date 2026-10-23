package com.BzNpXqL.vTmJrK.domain.model

data class Board(
    val cols: Int,
    val rows: Int,
    val cells: List<Cell>
) {
    fun cellAt(index: Int): Cell =
        if (index in cells.indices) cells[index] else Cell.Grass

    fun rotated(index: Int): Board {
        val cell = cellAt(index)
        if (cell !is Cell.Sign) return this
        val next = cells.toMutableList()
        next[index] = cell.copy(orientation = (cell.orientation + 1) % cell.shape.orientations)
        return copy(cells = next)
    }

    companion object {
        val EMPTY = Board(0, 0, emptyList())
    }
}
