package com.BzNpXqL.vTmJrK.presentation.menu

import com.BzNpXqL.vTmJrK.core.config.GameConfig
import com.BzNpXqL.vTmJrK.domain.model.SchemeProgress

enum class MenuDialog {
    None,
    LevelMap,
    Tutorial,
    Settings
}

data class MenuUiState(
    val schemeIndex: Int = 0,
    val schemeCount: Int = GameConfig.SCHEME_COUNT,
    val cols: Int = 5,
    val rows: Int = 5,
    val totalStars: Int = 0,
    val maxStars: Int = GameConfig.SCHEME_COUNT * GameConfig.STARS_PER_SCHEME,
    val bestScore: Int = 0,
    val unlocked: Int = 1,
    val schemes: List<SchemeProgress> = emptyList(),
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val dialog: MenuDialog = MenuDialog.None,
    val resetArmed: Boolean = false
)
