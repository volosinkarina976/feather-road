package com.BzNpXqL.vTmJrK.domain.usecase

import com.BzNpXqL.vTmJrK.domain.model.RoundResult
import com.BzNpXqL.vTmJrK.domain.model.Scheme

class ScoreRoundUseCase {

    operator fun invoke(
        win: Boolean,
        scheme: Scheme,
        routesDone: Int,
        routesTotal: Int,
        movesLeft: Int,
        movesUsed: Int,
        minMoves: Int,
        checksLeft: Int
    ): RoundResult {
        val score = routesDone * 250 + movesLeft * 40 + checksLeft * 100 +
            if (win) scheme.difficultyBonus else 0
        val stars = when {
            !win -> 0
            movesUsed <= minMoves + 1 -> 3
            movesUsed <= minMoves + scheme.slack / 2 -> 2
            else -> 1
        }
        return RoundResult(
            schemeIndex = scheme.index,
            win = win,
            score = score,
            stars = stars,
            movesUsed = movesUsed,
            movesLeft = movesLeft,
            routesDone = routesDone,
            routesTotal = routesTotal,
            newBest = false
        )
    }
}
