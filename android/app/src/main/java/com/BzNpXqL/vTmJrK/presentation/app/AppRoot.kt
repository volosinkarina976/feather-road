package com.BzNpXqL.vTmJrK.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.BzNpXqL.vTmJrK.core.di.ServiceLocator
import com.BzNpXqL.vTmJrK.core.navigation.Screen
import com.BzNpXqL.vTmJrK.core.theme.BgBase
import com.BzNpXqL.vTmJrK.presentation.game.GameRoute
import com.BzNpXqL.vTmJrK.presentation.gameover.GameOverRoute
import com.BzNpXqL.vTmJrK.presentation.menu.MenuRoute
import com.BzNpXqL.vTmJrK.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()

    BackHandler(enabled = screen != Screen.Menu && screen != Screen.Splash) {
        appViewModel.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgBase)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                when (targetState) {
                    is Screen.Game -> (slideInHorizontally(
                        animationSpec = tween(260),
                        initialOffsetX = { full -> full / 4 }
                    ) + fadeIn(tween(260))) togetherWith fadeOut(tween(200))

                    is Screen.GameOver -> (fadeIn(tween(320)) + scaleIn(
                        animationSpec = tween(320),
                        initialScale = 0.96f
                    )) togetherWith fadeOut(tween(220))

                    else -> fadeIn(tween(420)) togetherWith fadeOut(tween(300))
                }
            }
        ) { target ->
            when (target) {
                is Screen.Splash -> SplashRoute(
                    onFinished = { appViewModel.replaceWith(Screen.Menu) }
                )

                is Screen.Menu -> MenuRoute(
                    onPlay = { appViewModel.navigate(Screen.Game) }
                )

                is Screen.Game -> GameRoute(
                    onRoundFinished = { win -> appViewModel.replaceWith(Screen.GameOver(win)) }
                )

                is Screen.GameOver -> GameOverRoute(
                    win = target.win,
                    onReplay = { appViewModel.replaceWith(Screen.Game) },
                    onMenu = { appViewModel.backToMenu() }
                )
            }
        }
    }
}
