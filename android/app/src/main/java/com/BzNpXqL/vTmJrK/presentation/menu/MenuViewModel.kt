package com.BzNpXqL.vTmJrK.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.BzNpXqL.vTmJrK.data.sample.SampleData
import com.BzNpXqL.vTmJrK.domain.model.SchemeProgress
import com.BzNpXqL.vTmJrK.domain.repository.ProgressRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progress: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    private val _events = Channel<Unit>(Channel.BUFFERED)
    val events: Flow<Unit> = _events.receiveAsFlow()

    fun onScreenEnter() {
        refresh()
    }

    fun onScreenLeave() {
        _state.update { it.copy(dialog = MenuDialog.None, resetArmed = false) }
    }

    fun onPlay() {
        viewModelScope.launch { _events.send(Unit) }
    }

    fun onSelectScheme(index: Int) {
        if (index >= progress.unlockedCount()) return
        progress.setCurrentScheme(index)
        refresh()
        _state.update { it.copy(dialog = MenuDialog.None) }
        viewModelScope.launch { _events.send(Unit) }
    }

    fun onOpenLevelMap() {
        _state.update { it.copy(dialog = MenuDialog.LevelMap) }
    }

    fun onOpenTutorial() {
        _state.update { it.copy(dialog = MenuDialog.Tutorial) }
    }

    fun onOpenSettings() {
        _state.update { it.copy(dialog = MenuDialog.Settings, resetArmed = false) }
    }

    fun onDismissDialog() {
        _state.update { it.copy(dialog = MenuDialog.None, resetArmed = false) }
    }

    fun onToggleSound(enabled: Boolean) {
        progress.setSoundEnabled(enabled)
        _state.update { it.copy(soundEnabled = enabled) }
    }

    fun onToggleHaptics(enabled: Boolean) {
        progress.setHapticsEnabled(enabled)
        _state.update { it.copy(hapticsEnabled = enabled) }
    }

    fun onResetRequested() {
        if (!_state.value.resetArmed) {
            _state.update { it.copy(resetArmed = true) }
            return
        }
        progress.resetProgress()
        refresh()
        _state.update { it.copy(dialog = MenuDialog.None, resetArmed = false) }
    }

    private fun refresh() {
        val index = progress.currentScheme()
        val scheme = SampleData.schemeAt(index)
        val unlocked = progress.unlockedCount()
        _state.update { current ->
            current.copy(
                schemeIndex = index,
                cols = scheme.cols,
                rows = scheme.rows,
                totalStars = progress.totalStars(),
                bestScore = progress.bestScore(),
                unlocked = unlocked,
                schemes = SampleData.schemes.map { entry ->
                    SchemeProgress(
                        index = entry.index,
                        cols = entry.cols,
                        rows = entry.rows,
                        stars = progress.starsFor(entry.index),
                        unlocked = entry.index < unlocked
                    )
                },
                soundEnabled = progress.soundEnabled(),
                hapticsEnabled = progress.hapticsEnabled()
            )
        }
    }
}
