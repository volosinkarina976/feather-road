package com.BzNpXqL.vTmJrK.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.BzNpXqL.vTmJrK.R
import com.BzNpXqL.vTmJrK.core.di.ServiceLocator
import com.BzNpXqL.vTmJrK.core.theme.AccentGold
import com.BzNpXqL.vTmJrK.core.theme.BgDeep
import com.BzNpXqL.vTmJrK.core.theme.BgShadow
import com.BzNpXqL.vTmJrK.core.theme.BrassLight
import com.BzNpXqL.vTmJrK.core.theme.SurfaceCard
import com.BzNpXqL.vTmJrK.core.theme.TextMuted
import com.BzNpXqL.vTmJrK.core.theme.TextPrimary
import com.BzNpXqL.vTmJrK.core.theme.TextSecondary

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.factory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    SplashScreen()
}

@Composable
private fun SplashScreen() {
    val loop = rememberInfiniteTransition()
    val sway by loop.animateFloat(
        initialValue = -7f,
        targetValue = 7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val ring by loop.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val glow by loop.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val drift0 by moteDrift(loop, 3200)
    val drift1 by moteDrift(loop, 3600)
    val drift2 by moteDrift(loop, 4000)
    val drift3 by moteDrift(loop, 4400)
    val drift4 by moteDrift(loop, 4800)
    val drift5 by moteDrift(loop, 5200)
    val drifts = listOf(drift0, drift1, drift2, drift3, drift4, drift5)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_splash),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(BgDeep, BgShadow.copy(alpha = 0.80f), BgShadow)
                    )
                )
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            for (index in drifts.indices) {
                val phase = drifts[index]
                val x = size.width * (0.16f + 0.14f * index)
                val y = size.height * (0.72f - 0.5f * phase) - index * 14f
                drawCircle(
                    color = AccentGold.copy(alpha = 0.10f + 0.12f * (1f - phase)),
                    radius = 7f + index * 2.5f,
                    center = Offset(x, y)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .graphicsLayer {
                            scaleX = glow
                            scaleY = glow
                        }
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(AccentGold.copy(alpha = 0.18f), Color.Transparent)
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .graphicsLayer { rotationZ = ring }
                        .border(
                            width = 3.dp,
                            brush = Brush.sweepGradient(
                                listOf(
                                    AccentGold.copy(alpha = 0.05f),
                                    BrassLight,
                                    AccentGold.copy(alpha = 0.05f)
                                )
                            ),
                            shape = CircleShape
                        )
                )
                Image(
                    painter = painterResource(R.drawable.sprite_signpost),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(132.dp)
                        .graphicsLayer { rotationZ = sway }
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.displaySmall.copy(
                    shadow = Shadow(
                        color = AccentGold.copy(alpha = 0.40f),
                        offset = Offset(0f, 0f),
                        blurRadius = 24f
                    )
                ),
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.splash_tagline),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .width(168.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = AccentGold,
                trackColor = SurfaceCard
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun moteDrift(
    transition: androidx.compose.animation.core.InfiniteTransition,
    durationMillis: Int
) = transition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
        animation = tween(durationMillis, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
    )
)
