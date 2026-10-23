package com.BzNpXqL.vTmJrK.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.BzNpXqL.vTmJrK.core.config.GameConfig
import com.BzNpXqL.vTmJrK.data.sample.SampleData
import com.BzNpXqL.vTmJrK.domain.model.Cell
import com.BzNpXqL.vTmJrK.domain.model.RouteColor
import com.BzNpXqL.vTmJrK.domain.model.Scheme
import com.BzNpXqL.vTmJrK.domain.model.TraceResult
import com.BzNpXqL.vTmJrK.domain.repository.ProgressRepository
import com.BzNpXqL.vTmJrK.domain.usecase.BuildSchemeUseCase
import com.BzNpXqL.vTmJrK.domain.usecase.ScoreRoundUseCase
import com.BzNpXqL.vTmJrK.domain.usecase.TraceRoutesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val progress: ProgressRepository,
    private val buildScheme: BuildSchemeUseCase,
    private val traceRoutes: TraceRoutesUseCase,
    private val scoreRound: ScoreRoundUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<Boolean>(Channel.BUFFERED)
    val events: Flow<Boolean> = _events.receiveAsFlow()

    private var scheme: Scheme = SampleData.schemeAt(0)
    private var attempt = 0
    private var idleJob: Job? = null
    private var phaseJob: Job? = null

    fun onScreenEnter() {
        val phase = _state.value.phase
        val fresh = phase == Phase.Solved || phase == Phase.Failed ||
            _state.value.board.cells.isEmpty()
        if (fresh) startRound() else armIdle()
    }

    fun onScreenLeave() {
        cancelTimers()
    }

    fun onCellTap(index: Int) {
        val current = _state.value
        if (current.phase != Phase.Idle && current.phase != Phase.Routing) return
        if (current.board.cellAt(index) !is Cell.Sign) return
        if (current.movesLeft <= 0) return

        val turns = current.turns.toMutableList()
        turns[index] = turns[index] + 1
        val board = current.board.rotated(index)
        val trace = traceRoutes(board, current.routes)
        val movesLeft = current.movesLeft - 1
        val hint = when {
            trace.crossed.isNotEmpty() -> HintKind.Crossing
            trace.completed == current.routes.size -> HintKind.Ready
            else -> HintKind.Routing
        }
        _state.value = current.copy(
            phase = Phase.Routing,
            board = board,
            turns = turns,
            movesLeft = movesLeft,
            movesUsed = current.movesUsed + 1,
            routesDone = trace.completed,
            ownership = trace.ownership,
            completedCells = trace.completedCells,
            completedRoutes = completedRoutes(trace),
            crossedCells = trace.crossed,
            flashingCells = emptySet(),
            hint = hint
        )
        when {
            trace.solved -> resolveWin()
            movesLeft <= 0 -> resolveLoss()
            else -> armIdle()
        }
    }

    fun onCheckRoutes() {
        val current = _state.value
        if (current.phase != Phase.Idle && current.phase != Phase.Routing) return
        val trace = traceRoutes(current.board, current.routes)
        if (trace.solved) {
            resolveWin()
            return
        }
        val checksLeft = (current.checksLeft - 1).coerceAtLeast(0)
        val failing = trace.traces
            .filter { !it.complete }
            .flatMap { it.path }
            .toSet() + trace.crossed
        _state.value = current.copy(
            phase = Phase.Checking,
            checksLeft = checksLeft,
            flashingCells = failing
        )
        armIdle()
        phaseJob?.cancel()
        phaseJob = viewModelScope.launch {
            delay(GameConfig.CHECK_FREEZE_MS)
            if (checksLeft <= 0) {
                resolveLoss()
            } else {
                _state.update { entry ->
                    entry.copy(
                        phase = if (entry.movesUsed > 0) Phase.Routing else Phase.Idle,
                        flashingCells = emptySet()
                    )
                }
            }
        }
    }

    private fun startRound() {
        cancelTimers()
        val index = progress.currentScheme()
        scheme = SampleData.schemeAt(index)
        attempt += 1
        val built = buildScheme(scheme, attempt)
        val trace = traceRoutes(built.board, built.routes)
        _state.value = GameUiState(
            phase = Phase.Idle,
            board = built.board,
            turns = built.turns,
            routes = built.routes,
            movesLeft = built.moveLimit,
            minMoves = built.minMoves,
            movesUsed = 0,
            checksLeft = GameConfig.CHECK_LIMIT,
            routesDone = trace.completed,
            routesTotal = built.routes.size,
            ownership = trace.ownership,
            completedCells = trace.completedCells,
            completedRoutes = completedRoutes(trace),
            crossedCells = trace.crossed,
            flashingCells = emptySet(),
            schemeIndex = index,
            hint = HintKind.Start
        )
        armIdle()
    }

    private fun completedRoutes(trace: TraceResult): Set<RouteColor> =
        trace.traces
            .filter { item -> item.complete && item.path.none { trace.crossed.contains(it) } }
            .map { it.route }
            .toSet()

    private fun armIdle() {
        idleJob?.cancel()
        idleJob = viewModelScope.launch {
            delay(GameConfig.IDLE_TIMEOUT_MS)
            val phase = _state.value.phase
            if (phase == Phase.Idle || phase == Phase.Routing) resolveLoss()
        }
    }

    private fun cancelTimers() {
        idleJob?.cancel()
        idleJob = null
        phaseJob?.cancel()
        phaseJob = null
    }

    private fun resolveWin() {
        cancelTimers()
        _state.update { it.copy(phase = Phase.Solved, flashingCells = emptySet()) }
        phaseJob = viewModelScope.launch {
            delay(GameConfig.VICTORY_SWEEP_MS)
            finish(true)
        }
    }

    private fun resolveLoss() {
        cancelTimers()
        _state.update { it.copy(phase = Phase.Failed, flashingCells = emptySet()) }
        phaseJob = viewModelScope.launch {
            delay(GameConfig.FAIL_SHAKE_MS)
            finish(false)
        }
    }

    private fun finish(win: Boolean) {
        val snapshot = _state.value
        val result = scoreRound(
            win = win,
            scheme = scheme,
            routesDone = snapshot.routesDone,
            routesTotal = snapshot.routesTotal,
            movesLeft = snapshot.movesLeft,
            movesUsed = snapshot.movesUsed,
            minMoves = snapshot.minMoves,
            checksLeft = snapshot.checksLeft
        )
        progress.saveRound(result)
        _events.trySend(win)
    }
}
