package com.BzNpXqL.vTmJrK.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.BzNpXqL.vTmJrK.core.theme.AccentGold
import com.BzNpXqL.vTmJrK.core.theme.BoardWoodBottom
import com.BzNpXqL.vTmJrK.core.theme.BoardWoodTop
import com.BzNpXqL.vTmJrK.core.theme.BrassDark
import com.BzNpXqL.vTmJrK.core.theme.BrassLight

@Composable
fun WoodPanel(
    modifier: Modifier = Modifier,
    corner: Dp = 18.dp,
    borderWidth: Dp = 2.dp,
    inset: Dp = 6.dp,
    showStuds: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(corner)
    Box(
        modifier = modifier
            .shadow(14.dp, shape)
            .clip(shape)
            .background(Brush.linearGradient(listOf(BoardWoodTop, BoardWoodBottom)))
            .border(borderWidth, AccentGold.copy(alpha = 0.45f), shape)
            .padding(borderWidth + inset)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val lines = 4
            for (line in 1..lines) {
                val y = size.height * line / (lines + 1f)
                drawLine(
                    color = Color.Black.copy(alpha = 0.08f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 2f
                )
            }
            if (showStuds) {
                val pad = 9f
                val radius = 5f
                val spots = listOf(
                    Offset(pad, pad),
                    Offset(size.width - pad, pad),
                    Offset(pad, size.height - pad),
                    Offset(size.width - pad, size.height - pad)
                )
                for (spot in spots) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(BrassLight, BrassDark),
                            center = spot,
                            radius = radius
                        ),
                        radius = radius,
                        center = spot
                    )
                }
            }
        }
        content()
    }
}
