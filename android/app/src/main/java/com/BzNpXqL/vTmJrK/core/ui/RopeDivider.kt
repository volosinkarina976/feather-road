package com.BzNpXqL.vTmJrK.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.BzNpXqL.vTmJrK.core.theme.BrassDark
import com.BzNpXqL.vTmJrK.core.theme.BrassLight

@Composable
fun RopeDivider(modifier: Modifier = Modifier, strands: Int = 10) {
    Canvas(modifier = modifier.height(6.dp)) {
        val step = size.width / strands
        for (index in 0 until strands) {
            val startX = index * step
            val even = index % 2 == 0
            drawLine(
                color = (if (even) BrassDark else BrassLight).copy(alpha = 0.55f),
                start = Offset(startX, if (even) 0f else size.height),
                end = Offset(startX + step, if (even) size.height else 0f),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }
    }
}
