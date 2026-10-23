package com.BzNpXqL.vTmJrK.presentation.dialog

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.BzNpXqL.vTmJrK.R
import com.BzNpXqL.vTmJrK.core.theme.AccentCyan
import com.BzNpXqL.vTmJrK.core.theme.AccentGold
import com.BzNpXqL.vTmJrK.core.theme.AccentGreen
import com.BzNpXqL.vTmJrK.core.theme.SurfaceCard
import com.BzNpXqL.vTmJrK.core.theme.SurfaceRaised
import com.BzNpXqL.vTmJrK.core.theme.TextPrimary
import com.BzNpXqL.vTmJrK.core.theme.TextSecondary
import com.BzNpXqL.vTmJrK.core.ui.PrimaryButton
import com.BzNpXqL.vTmJrK.core.ui.WoodPanel

@Composable
fun TutorialDialog(onDismiss: () -> Unit) {
    val rules = listOf(
        stringResource(R.string.rule_one),
        stringResource(R.string.rule_two),
        stringResource(R.string.rule_three),
        stringResource(R.string.rule_four)
    )
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.verticalGradient(listOf(SurfaceRaised, SurfaceCard)))
                .border(1.dp, AccentGold.copy(alpha = 0.30f), RoundedCornerShape(24.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.dialog_rules),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            DemoLane()
            Spacer(modifier = Modifier.height(18.dp))
            for (index in rules.indices) {
                key(index) {
                    Text(
                        text = rules[index],
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            PrimaryButton(
                label = stringResource(R.string.cta_got_it),
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DemoLane() {
    val loop = rememberInfiniteTransition()
    val spin by loop.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    WoodPanel(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        corner = 16.dp
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AccentGold.copy(alpha = 0.26f))
                    .border(2.dp, AccentGold.copy(alpha = 0.75f), CircleShape)
            )
            Image(
                painter = painterResource(R.drawable.sprite_signpost),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(56.dp)
                    .graphicsLayer { rotationZ = spin }
            )
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AccentGreen.copy(alpha = 0.26f))
                    .border(2.dp, AccentCyan.copy(alpha = 0.75f), CircleShape)
            )
        }
    }
}
