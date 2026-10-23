package com.BzNpXqL.vTmJrK.data.sample

import com.BzNpXqL.vTmJrK.domain.model.RouteColor
import com.BzNpXqL.vTmJrK.domain.model.Scheme

object SampleData {

    val schemes: List<Scheme> = listOf(
        Scheme(index = 0, cols = 5, rows = 5, pairs = 3, slack = 8, seed = 1741, difficultyBonus = 0),
        Scheme(index = 1, cols = 5, rows = 5, pairs = 3, slack = 8, seed = 2298, difficultyBonus = 0),
        Scheme(index = 2, cols = 5, rows = 5, pairs = 3, slack = 8, seed = 3615, difficultyBonus = 0),
        Scheme(index = 3, cols = 5, rows = 5, pairs = 3, slack = 8, seed = 4480, difficultyBonus = 0),
        Scheme(index = 4, cols = 6, rows = 6, pairs = 4, slack = 6, seed = 5127, difficultyBonus = 150),
        Scheme(index = 5, cols = 6, rows = 6, pairs = 4, slack = 6, seed = 6034, difficultyBonus = 150),
        Scheme(index = 6, cols = 6, rows = 6, pairs = 4, slack = 6, seed = 7259, difficultyBonus = 150),
        Scheme(index = 7, cols = 6, rows = 6, pairs = 4, slack = 6, seed = 8366, difficultyBonus = 150),
        Scheme(index = 8, cols = 7, rows = 7, pairs = 4, slack = 4, seed = 9143, difficultyBonus = 300),
        Scheme(index = 9, cols = 7, rows = 7, pairs = 4, slack = 4, seed = 10572, difficultyBonus = 300),
        Scheme(index = 10, cols = 7, rows = 7, pairs = 4, slack = 4, seed = 11688, difficultyBonus = 300),
        Scheme(index = 11, cols = 7, rows = 7, pairs = 4, slack = 4, seed = 12901, difficultyBonus = 300)
    )

    val routeOrder: List<RouteColor> = listOf(
        RouteColor.Amber,
        RouteColor.Cyan,
        RouteColor.Moss,
        RouteColor.Clay
    )

    fun schemeAt(index: Int): Scheme = schemes[index.coerceIn(0, schemes.size - 1)]
}
