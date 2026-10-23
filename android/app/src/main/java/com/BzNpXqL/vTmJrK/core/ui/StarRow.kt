package com.BzNpXqL.vTmJrK.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.BzNpXqL.vTmJrK.R

@Composable
fun StarRow(
    earned: Int,
    total: Int,
    description: String,
    modifier: Modifier = Modifier,
    starSize: Dp = 44.dp,
    gap: Dp = 10.dp
) {
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (slot in 0 until total) {
            val filled = slot < earned
            val pop by animateFloatAsState(
                targetValue = if (filled) 1f else 0.86f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            Image(
                painter = painterResource(R.drawable.sprite_star),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(starSize)
                    .graphicsLayer {
                        scaleX = pop
                        scaleY = pop
                    }
                    .alpha(if (filled) 1f else 0.18f)
            )
        }
    }
}
