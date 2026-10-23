package com.BzNpXqL.vTmJrK.domain.model

enum class SignShape(val orientations: Int) {
    Straight(2),
    Elbow(4);

    fun edges(orientation: Int): Set<Edge> {
        val step = ((orientation % orientations) + orientations) % orientations
        return when (this) {
            Straight -> if (step == 0) setOf(Edge.North, Edge.South) else setOf(Edge.East, Edge.West)
            Elbow -> when (step) {
                0 -> setOf(Edge.North, Edge.East)
                1 -> setOf(Edge.East, Edge.South)
                2 -> setOf(Edge.South, Edge.West)
                else -> setOf(Edge.West, Edge.North)
            }
        }
    }

    companion object {
        fun forEdges(first: Edge, second: Edge): SignShape =
            if (first == second.opposite) Straight else Elbow

        fun orientationFor(shape: SignShape, first: Edge, second: Edge): Int {
            val pair = setOf(first, second)
            for (step in 0 until shape.orientations) {
                if (shape.edges(step) == pair) return step
            }
            return 0
        }
    }
}
