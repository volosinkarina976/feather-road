package com.BzNpXqL.vTmJrK.presentation.gameover

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.BzNpXqL.vTmJrK.R
import com.BzNpXqL.vTmJrK.core.di.ServiceLocator
import com.BzNpXqL.vTmJrK.core.theme.AccentCyan
import com.BzNpXqL.vTmJrK.core.theme.AccentGold
import com.BzNpXqL.vTmJrK.core.theme.AccentGreen
import com.BzNpXqL.vTmJrK.core.theme.AccentTerracotta
import com.BzNpXqL.vTmJrK.core.theme.BgBase
import com.BzNpXqL.vTmJrK.core.theme.NumericStyle
import com.BzNpXqL.vTmJrK.core.theme.OnAccent
import com.BzNpXqL.vTmJrK.core.theme.SurfaceRaised
import com.BzNpXqL.vTmJrK.core.theme.TextSecondary
import com.BzNpXqL.vTmJrK.core.ui.PrimaryButton
import com.BzNpXqL.vTmJrK.core.ui.RopeDivider
import com.BzNpXqL.vTmJrK.core.ui.SecondaryButton
import com.BzNpXqL.vTmJrK.core.ui.StarRow
import com.BzNpXqL.vTmJrK.core.ui.StatCard

@Composable
fun GameOverRoute(
    win: Boolean,
    onReplay: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.factory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        gameOverViewModel.onScreenEnter()
        onDispose { }
    }

    LaunchedEffect(Unit) {
        gameOverViewModel.events.collect { onReplay() }
    }

    GameOverScreen(
        win = win,
        state = state,
        onReplay = gameOverViewModel::onReplay,
        onNextScheme = gameOverViewModel::onNextScheme,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverScreen(
    win: Boolean,
    state: GameOverUiState,
    onReplay: () -> Unit,
    onNextScheme: () -> Unit,
    onMenu: () -> Unit
) {
    val loop = rememberInfiniteTransition()
    val breathe by loop.animateFloat(
        initialValue = 1f,
        targetValue = if (win) 1.06f else 1.015f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val accent = if (win) AccentGreen else AccentTerracotta
    val overlay = if (win) {
        listOf(SurfaceRaised.copy(alpha = 0.80f), Color(0xE6243024), BgBase)
    } else {
        listOf(SurfaceRaised.copy(alpha = 0.80f), Color(0xE6302420), BgBase)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_result),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(overlay))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.graphicsLayer {
                    scaleX = breathe
                    scaleY = breathe
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_hen),
                    contentDescription = stringResource(R.string.cd_hen),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(140.dp)
                        .alpha(if (win) 1f else 0.72f)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Image(
                    painter = painterResource(R.drawable.sprite_feeder),
                    contentDescription = stringResource(R.string.cd_feeder),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(96.dp)
                        .alpha(if (win) 1f else 0.72f)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            if (state.newBest) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(Brush.horizontalGradient(listOf(AccentGold, AccentGold)))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.new_best),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = OnAccent
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Text(
                text = stringResource(if (win) R.string.result_win else R.string.result_loss),
                style = MaterialTheme.typography.displaySmall,
                color = accent,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(
                    if (win) R.string.result_sub_win else R.string.result_sub_loss
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            RopeDivider(modifier = Modifier.width(200.dp))
            Spacer(modifier = Modifier.height(18.dp))
            StarRow(
                earned = state.stars,
                total = state.starsPerScheme,
                description = stringResource(
                    R.string.cd_stars,
                    state.stars,
                    state.starsPerScheme
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            ResultStats(state = state)
            Spacer(modifier = Modifier.height(24.dp))
            if (win && state.hasNextScheme) {
                PrimaryButton(
                    label = stringResource(R.string.cta_next_scheme),
                    onClick = onNextScheme,
                    iconRes = R.drawable.ic_play,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                SecondaryButton(
                    label = stringResource(R.string.cta_play_again),
                    onClick = onReplay,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                PrimaryButton(
                    label = stringResource(R.string.cta_play_again),
                    onClick = onReplay,
                    iconRes = R.drawable.ic_refresh,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(
                label = stringResource(R.string.cta_menu),
                onClick = onMenu,
                accent = AccentCyan,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ResultStats(state: GameOverUiState) {
    val showScore = state.score > 0
    val showTurns = state.movesLeft > 0
    val showRoutes = state.routesDone > 0
    val visible = listOf(showScore, showTurns, showRoutes).count { it }

    if (visible >= 2) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (showScore) {
                StatCard(
                    value = state.score.toString(),
                    label = stringResource(R.string.stat_score),
                    accent = AccentGold,
                    modifier = Modifier.weight(1f)
                )
            }
            if (showTurns) {
                StatCard(
                    value = state.movesLeft.toString(),
                    label = stringResource(R.string.stat_turns_left),
                    accent = AccentCyan,
                    modifier = Modifier.weight(1f)
                )
            }
            if (showRoutes) {
                StatCard(
                    value = stringResource(
                        R.string.value_pair,
                        state.routesDone,
                        state.routesTotal
                    ),
                    label = stringResource(R.string.stat_routes),
                    accent = AccentGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceRaised.copy(alpha = 0.85f))
                .border(1.dp, AccentGold.copy(alpha = 0.30f), RoundedCornerShape(16.dp))
                .padding(horizontal = 22.dp, vertical = 14.dp)
        ) {
            Text(
                text = stringResource(R.string.score_line, state.score),
                style = NumericStyle,
                color = AccentGold,
                textAlign = TextAlign.Center
            )
        }
    }
}
