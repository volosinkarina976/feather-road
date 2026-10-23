package com.BzNpXqL.vTmJrK.domain.repository

import com.BzNpXqL.vTmJrK.domain.model.RoundResult

interface ProgressRepository {
    fun currentScheme(): Int
    fun setCurrentScheme(index: Int)
    fun unlockedCount(): Int
    fun starsFor(index: Int): Int
    fun totalStars(): Int
    fun bestScore(): Int
    fun soundEnabled(): Boolean
    fun hapticsEnabled(): Boolean
    fun setSoundEnabled(enabled: Boolean)
    fun setHapticsEnabled(enabled: Boolean)
    fun saveRound(result: RoundResult): RoundResult
    fun lastResult(): RoundResult
    fun resetProgress()
}
