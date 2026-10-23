package com.BzNpXqL.vTmJrK.domain.model

enum class Edge(val dx: Int, val dy: Int) {
    North(0, -1),
    East(1, 0),
    South(0, 1),
    West(-1, 0);

    val opposite: Edge
        get() = when (this) {
            North -> South
            East -> West
            South -> North
            West -> East
        }
}
