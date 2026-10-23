package com.BzNpXqL.vTmJrK.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.BzNpXqL.vTmJrK.core.config.GameConfig
import com.BzNpXqL.vTmJrK.domain.repository.ProgressRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class GameOverUiState(
    val score: Int = 0,
    val stars: Int = 0,
    val starsPerScheme: Int = GameConfig.STARS_PER_SCHEME,
    val movesLeft: Int = 0,
    val routesDone: Int = 0,
    val routesTotal: Int = 0,
    val bestScore: Int = 0,
    val newBest: Boolean = false,
    val hasNextScheme: Boolean = false
)

class GameOverViewModel(
    private val progress: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    private val _events = Channel<Unit>(Channel.BUFFERED)
    val events: Flow<Unit> = _events.receiveAsFlow()

    fun onScreenEnter() {
        val result = progress.lastResult()
        val next = result.schemeIndex + 1
        _state.value = GameOverUiState(
            score = result.score,
            stars = result.stars,
            movesLeft = result.movesLeft,
            routesDone = result.routesDone,
            routesTotal = result.routesTotal,
            bestScore = progress.bestScore(),
            newBest = result.newBest,
            hasNextScheme = result.win && next < GameConfig.SCHEME_COUNT &&
                next < progress.unlockedCount()
        )
    }

    fun onReplay() {
        viewModelScope.launch { _events.send(Unit) }
    }

    fun onNextScheme() {
        progress.setCurrentScheme(progress.currentScheme() + 1)
        viewModelScope.launch { _events.send(Unit) }
    }
}
