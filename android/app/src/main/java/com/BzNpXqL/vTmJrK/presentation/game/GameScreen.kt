package com.BzNpXqL.vTmJrK.presentation.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.BzNpXqL.vTmJrK.R
import com.BzNpXqL.vTmJrK.core.di.ServiceLocator
import com.BzNpXqL.vTmJrK.core.theme.AccentCyan
import com.BzNpXqL.vTmJrK.core.theme.AccentGold
import com.BzNpXqL.vTmJrK.core.theme.AccentTerracotta
import com.BzNpXqL.vTmJrK.core.theme.BgBase
import com.BzNpXqL.vTmJrK.core.theme.NumericStyle
import com.BzNpXqL.vTmJrK.core.theme.SurfaceCard
import com.BzNpXqL.vTmJrK.core.theme.SurfaceRaised
import com.BzNpXqL.vTmJrK.core.theme.TextMuted
import com.BzNpXqL.vTmJrK.core.theme.TextPrimary
import com.BzNpXqL.vTmJrK.core.theme.TextSecondary
import com.BzNpXqL.vTmJrK.core.ui.PrimaryButton
import com.BzNpXqL.vTmJrK.core.ui.RopeDivider
import com.BzNpXqL.vTmJrK.core.ui.StatCard

@Composable
fun GameRoute(onRoundFinished: (Boolean) -> Unit) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        gameViewModel.onScreenEnter()
        onDispose { gameViewModel.onScreenLeave() }
    }

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { win -> onRoundFinished(win) }
    }

    GameScreen(
        state = state,
        onCellTap = gameViewModel::onCellTap,
        onCheck = gameViewModel::onCheckRoutes
    )
}

@Composable
private fun GameScreen(
    state: GameUiState,
    onCellTap: (Int) -> Unit,
    onCheck: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            BgBase.copy(alpha = 0.80f),
                            SurfaceRaised.copy(alpha = 0.90f),
                            BgBase.copy(alpha = 0.95f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(state = state)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                RouteRibbon(state = state)
                Spacer(modifier = Modifier.height(18.dp))
                BoardView(
                    state = state,
                    onCellTap = onCellTap,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                RopeDivider(modifier = Modifier.width(180.dp))
            }
            GamePanel(state = state, onCheck = onCheck)
        }
    }
}

@Composable
private fun GameHeader(state: GameUiState) {
    val lowMoves = state.movesLeft <= 3
    val movesTint by animateColorAsState(
        targetValue = if (lowMoves) AccentTerracotta else AccentGold,
        animationSpec = tween(220)
    )
    val movesLabel = stringResource(R.string.moves_pill, state.movesLeft)
    val movesState = stringResource(R.string.sd_moves_left, state.movesLeft)
    val strikesLabel = stringResource(R.string.cd_checks, state.checksLeft)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.Black.copy(alpha = 0.30f))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(1.dp)
                .background(AccentGold.copy(alpha = 0.16f))
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.stat_scheme),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Text(
                text = stringResource(
                    R.string.value_pair,
                    state.schemeIndex + 1,
                    state.schemeCount
                ),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceCard)
                .border(1.dp, movesTint.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .semantics {
                    stateDescription = movesState
                    liveRegion = LiveRegionMode.Polite
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_refresh),
                contentDescription = null,
                tint = movesTint,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = movesLabel,
                style = NumericStyle,
                fontSize = 14.sp,
                color = movesTint,
                maxLines = 1
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .clearAndSetSemantics { contentDescription = strikesLabel },
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (pip in 0 until 3) {
                val spent = pip >= state.checksLeft
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(
                            if (spent) AccentTerracotta
                            else TextPrimary.copy(alpha = 0.18f)
                        )
                        .border(
                            2.dp,
                            if (spent) TextPrimary.copy(alpha = 0.30f) else Color.Transparent,
                            CircleShape
                        )
                )
            }
        }
    }
}

@Composable
private fun RouteRibbon(state: GameUiState) {
    if (state.routes.isEmpty()) return
    val ribbonLabel = stringResource(
        R.string.cd_routes,
        state.routesDone,
        state.routesTotal
    )
    Row(
        modifier = Modifier.clearAndSetSemantics { contentDescription = ribbonLabel },
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (route in state.routes) {
            key(route) {
            val done = state.completedRoutes.contains(route)
            val tint = routeTint(route)
            val lift by animateFloatAsState(
                targetValue = if (done) 1f else 0.9f,
                animationSpec = tween(260)
            )
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .graphicsLayer {
                        scaleX = lift
                        scaleY = lift
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(tint.copy(alpha = if (done) 0.85f else 0.16f))
                    .border(
                        1.dp,
                        tint.copy(alpha = if (done) 0.95f else 0.40f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (done) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = BgBase,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            }
        }
    }
}

@Composable
private fun GamePanel(state: GameUiState, onCheck: () -> Unit) {
    val shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    val hintText = when (state.hint) {
        HintKind.Start -> stringResource(R.string.hint_start)
        HintKind.Routing -> stringResource(R.string.hint_routing)
        HintKind.Ready -> stringResource(R.string.hint_ready)
        HintKind.Crossing -> stringResource(R.string.hint_crossing)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, shape)
            .clip(shape)
            .background(SurfaceRaised.copy(alpha = 0.97f))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(1.dp)
                .background(AccentGold.copy(alpha = 0.22f))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 14.dp, bottom = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    value = stringResource(
                        R.string.value_pair,
                        state.routesDone,
                        state.routesTotal
                    ),
                    label = stringResource(R.string.stat_routes),
                    accent = AccentCyan,
                    compact = true,
                    announcement = stringResource(
                        R.string.cd_routes,
                        state.routesDone,
                        state.routesTotal
                    ),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = state.movesLeft.toString(),
                    label = stringResource(R.string.stat_moves),
                    accent = AccentGold,
                    compact = true,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = state.checksLeft.toString(),
                    label = stringResource(R.string.stat_checks),
                    accent = AccentTerracotta,
                    compact = true,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = hintText,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = TextMuted,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            PrimaryButton(
                label = stringResource(R.string.cta_check),
                onClick = onCheck,
                iconRes = R.drawable.ic_check,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
