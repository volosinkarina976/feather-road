package com.BzNpXqL.vTmJrK.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.BzNpXqL.vTmJrK.R
import com.BzNpXqL.vTmJrK.core.di.ServiceLocator
import com.BzNpXqL.vTmJrK.core.theme.AccentCyan
import com.BzNpXqL.vTmJrK.core.theme.AccentGold
import com.BzNpXqL.vTmJrK.core.theme.AccentGreen
import com.BzNpXqL.vTmJrK.core.theme.BgBase
import com.BzNpXqL.vTmJrK.core.theme.SurfaceRaised
import com.BzNpXqL.vTmJrK.core.theme.TextMuted
import com.BzNpXqL.vTmJrK.core.theme.TextPrimary
import com.BzNpXqL.vTmJrK.core.theme.TextSecondary
import com.BzNpXqL.vTmJrK.core.ui.PrimaryButton
import com.BzNpXqL.vTmJrK.core.ui.SecondaryButton
import com.BzNpXqL.vTmJrK.core.ui.StatCard
import com.BzNpXqL.vTmJrK.presentation.dialog.LevelMapDialog
import com.BzNpXqL.vTmJrK.presentation.dialog.SettingsDialog
import com.BzNpXqL.vTmJrK.presentation.dialog.TutorialDialog

@Composable
fun MenuRoute(onPlay: () -> Unit) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.factory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        menuViewModel.onScreenEnter()
        onDispose { menuViewModel.onScreenLeave() }
    }

    LaunchedEffect(Unit) {
        menuViewModel.events.collect { onPlay() }
    }

    MenuScreen(
        state = state,
        onPlay = menuViewModel::onPlay,
        onLevelMap = menuViewModel::onOpenLevelMap,
        onRules = menuViewModel::onOpenTutorial,
        onSettings = menuViewModel::onOpenSettings
    )

    when (state.dialog) {
        MenuDialog.LevelMap -> LevelMapDialog(
            schemes = state.schemes,
            current = state.schemeIndex,
            onSelect = menuViewModel::onSelectScheme,
            onDismiss = menuViewModel::onDismissDialog
        )

        MenuDialog.Tutorial -> TutorialDialog(onDismiss = menuViewModel::onDismissDialog)

        MenuDialog.Settings -> SettingsDialog(
            soundEnabled = state.soundEnabled,
            hapticsEnabled = state.hapticsEnabled,
            resetArmed = state.resetArmed,
            onToggleSound = menuViewModel::onToggleSound,
            onToggleHaptics = menuViewModel::onToggleHaptics,
            onReset = menuViewModel::onResetRequested,
            onDismiss = menuViewModel::onDismissDialog
        )

        MenuDialog.None -> Unit
    }
}

@Composable
private fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLevelMap: () -> Unit,
    onRules: () -> Unit,
    onSettings: () -> Unit
) {
    val loop = rememberInfiniteTransition()
    val henFloat by loop.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val signFloat by loop.animateFloat(
        initialValue = 5f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, delayMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_menu),
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
                            BgBase.copy(alpha = 0.45f),
                            Color.Transparent,
                            BgBase.copy(alpha = 0.70f),
                            BgBase
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_hen),
                    contentDescription = stringResource(R.string.cd_hen),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.dp)
                        .offset(y = henFloat.dp)
                        .size(150.dp)
                )
                Image(
                    painter = painterResource(R.drawable.sprite_signpost),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 28.dp, bottom = 24.dp)
                        .offset(y = signFloat.dp)
                        .size(96.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, SurfaceRaised.copy(alpha = 0.90f))
                            )
                        )
                )
            }
            MenuSheet(
                state = state,
                onPlay = onPlay,
                onLevelMap = onLevelMap,
                onRules = onRules,
                onSettings = onSettings
            )
        }
    }
}

@Composable
private fun MenuSheet(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLevelMap: () -> Unit,
    onRules: () -> Unit,
    onSettings: () -> Unit
) {
    val shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(18.dp, shape)
            .clip(shape)
            .background(SurfaceRaised.copy(alpha = 0.97f))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(1.dp)
                .background(AccentGold.copy(alpha = 0.28f))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(AccentGold.copy(alpha = 0.40f))
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.menu_tagline),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    value = stringResource(
                        R.string.value_pair,
                        state.schemeIndex + 1,
                        state.schemeCount
                    ),
                    label = stringResource(R.string.stat_scheme),
                    accent = AccentGold,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = stringResource(R.string.value_grid, state.cols, state.rows),
                    label = stringResource(R.string.stat_grid),
                    accent = AccentCyan,
                    modifier = Modifier.weight(1f)
                )
                if (state.totalStars > 0) {
                    StatCard(
                        value = stringResource(
                            R.string.value_pair,
                            state.totalStars,
                            state.maxStars
                        ),
                        label = stringResource(R.string.stat_stars),
                        accent = AccentGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(R.string.menu_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            PrimaryButton(
                label = stringResource(R.string.cta_play),
                onClick = onPlay,
                iconRes = R.drawable.ic_play,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    label = stringResource(R.string.cta_level_map),
                    onClick = onLevelMap,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    label = stringResource(R.string.cta_game_rules),
                    onClick = onRules,
                    accent = AccentCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        IconButton(
            onClick = onSettings,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 6.dp, top = 4.dp)
                .size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = stringResource(R.string.cd_settings),
                tint = TextSecondary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
