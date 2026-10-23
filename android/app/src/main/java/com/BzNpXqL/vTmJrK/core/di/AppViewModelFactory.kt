package com.BzNpXqL.vTmJrK.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.BzNpXqL.vTmJrK.domain.repository.ProgressRepository
import com.BzNpXqL.vTmJrK.domain.usecase.BuildSchemeUseCase
import com.BzNpXqL.vTmJrK.domain.usecase.ScoreRoundUseCase
import com.BzNpXqL.vTmJrK.domain.usecase.TraceRoutesUseCase
import com.BzNpXqL.vTmJrK.presentation.app.AppViewModel
import com.BzNpXqL.vTmJrK.presentation.game.GameViewModel
import com.BzNpXqL.vTmJrK.presentation.gameover.GameOverViewModel
import com.BzNpXqL.vTmJrK.presentation.menu.MenuViewModel
import com.BzNpXqL.vTmJrK.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val progress: ProgressRepository
) : ViewModelProvider.Factory {

    private val buildScheme = BuildSchemeUseCase()
    private val traceRoutes = TraceRoutesUseCase()
    private val scoreRound = ScoreRoundUseCase()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(progress)
            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(progress, buildScheme, traceRoutes, scoreRound)
            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(progress)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }
}
