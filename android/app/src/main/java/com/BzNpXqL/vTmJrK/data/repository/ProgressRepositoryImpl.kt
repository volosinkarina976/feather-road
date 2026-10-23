package com.BzNpXqL.vTmJrK.data.repository

import com.BzNpXqL.vTmJrK.core.config.GameConfig
import com.BzNpXqL.vTmJrK.data.local.PreferencesStore
import com.BzNpXqL.vTmJrK.domain.model.RoundResult
import com.BzNpXqL.vTmJrK.domain.repository.ProgressRepository

class ProgressRepositoryImpl(
    private val store: PreferencesStore
) : ProgressRepository {

    override fun currentScheme(): Int =
        store.readInt(KEY_CURRENT, 0).coerceIn(0, GameConfig.SCHEME_COUNT - 1)

    override fun setCurrentScheme(index: Int) {
        store.writeInt(KEY_CURRENT, index.coerceIn(0, GameConfig.SCHEME_COUNT - 1))
    }

    override fun unlockedCount(): Int =
        store.readInt(KEY_UNLOCKED, 1).coerceIn(1, GameConfig.SCHEME_COUNT)

    override fun starsFor(index: Int): Int =
        store.readInt(KEY_STARS + index, 0).coerceIn(0, GameConfig.STARS_PER_SCHEME)

    override fun totalStars(): Int {
        var total = 0
        for (index in 0 until GameConfig.SCHEME_COUNT) {
            total += starsFor(index)
        }
        return total
    }

    override fun bestScore(): Int = store.readInt(KEY_BEST, 0)

    override fun soundEnabled(): Boolean = store.readBoolean(KEY_SOUND, true)

    override fun hapticsEnabled(): Boolean = store.readBoolean(KEY_HAPTICS, true)

    override fun setSoundEnabled(enabled: Boolean) {
        store.writeBoolean(KEY_SOUND, enabled)
    }

    override fun setHapticsEnabled(enabled: Boolean) {
        store.writeBoolean(KEY_HAPTICS, enabled)
    }

    override fun saveRound(result: RoundResult): RoundResult {
        val newBest = result.score > bestScore()
        if (newBest) store.writeInt(KEY_BEST, result.score)
        if (result.stars > starsFor(result.schemeIndex)) {
            store.writeInt(KEY_STARS + result.schemeIndex, result.stars)
        }
        if (result.win && result.stars > 0) {
            val target = (result.schemeIndex + 2).coerceAtMost(GameConfig.SCHEME_COUNT)
            if (target > unlockedCount()) store.writeInt(KEY_UNLOCKED, target)
        }
        val stored = result.copy(newBest = newBest)
        store.writeInt(KEY_LAST_SCHEME, stored.schemeIndex)
        store.writeBoolean(KEY_LAST_WIN, stored.win)
        store.writeInt(KEY_LAST_SCORE, stored.score)
        store.writeInt(KEY_LAST_STARS, stored.stars)
        store.writeInt(KEY_LAST_MOVES_USED, stored.movesUsed)
        store.writeInt(KEY_LAST_MOVES_LEFT, stored.movesLeft)
        store.writeInt(KEY_LAST_ROUTES_DONE, stored.routesDone)
        store.writeInt(KEY_LAST_ROUTES_TOTAL, stored.routesTotal)
        store.writeBoolean(KEY_LAST_NEW_BEST, stored.newBest)
        return stored
    }

    override fun lastResult(): RoundResult = RoundResult(
        schemeIndex = store.readInt(KEY_LAST_SCHEME, 0),
        win = store.readBoolean(KEY_LAST_WIN, false),
        score = store.readInt(KEY_LAST_SCORE, 0),
        stars = store.readInt(KEY_LAST_STARS, 0),
        movesUsed = store.readInt(KEY_LAST_MOVES_USED, 0),
        movesLeft = store.readInt(KEY_LAST_MOVES_LEFT, 0),
        routesDone = store.readInt(KEY_LAST_ROUTES_DONE, 0),
        routesTotal = store.readInt(KEY_LAST_ROUTES_TOTAL, 0),
        newBest = store.readBoolean(KEY_LAST_NEW_BEST, false)
    )

    override fun resetProgress() {
        store.wipe()
    }

    private companion object {
        const val KEY_CURRENT = "current_scheme"
        const val KEY_UNLOCKED = "unlocked_schemes"
        const val KEY_STARS = "stars_"
        const val KEY_BEST = "best_score"
        const val KEY_SOUND = "sound_enabled"
        const val KEY_HAPTICS = "haptics_enabled"
        const val KEY_LAST_SCHEME = "last_scheme"
        const val KEY_LAST_WIN = "last_win"
        const val KEY_LAST_SCORE = "last_score"
        const val KEY_LAST_STARS = "last_stars"
        const val KEY_LAST_MOVES_USED = "last_moves_used"
        const val KEY_LAST_MOVES_LEFT = "last_moves_left"
        const val KEY_LAST_ROUTES_DONE = "last_routes_done"
        const val KEY_LAST_ROUTES_TOTAL = "last_routes_total"
        const val KEY_LAST_NEW_BEST = "last_new_best"
    }
}
