package com.BzNpXqL.vTmJrK.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.BzNpXqL.vTmJrK.core.theme.NumericStyle
import com.BzNpXqL.vTmJrK.core.theme.SurfaceCard
import com.BzNpXqL.vTmJrK.core.theme.TextMuted

@Composable
fun StatCard(
    value: String,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    announcement: String? = null
) {
    val shape = RoundedCornerShape(14.dp)
    val semanticsModifier = if (announcement != null) {
        Modifier.semantics {
            stateDescription = announcement
            liveRegion = LiveRegionMode.Polite
        }
    } else {
        Modifier
    }
    Column(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(SurfaceCard, SurfaceCard.copy(alpha = 0.78f))
                )
            )
            .border(1.dp, accent.copy(alpha = 0.35f), shape)
            .padding(horizontal = 10.dp, vertical = if (compact) 8.dp else 12.dp)
            .then(semanticsModifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(accent)
        )
        Spacer(modifier = Modifier.height(if (compact) 4.dp else 6.dp))
        Text(
            text = value,
            style = NumericStyle,
            fontSize = if (compact) 17.sp else 22.sp,
            color = accent,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = TextMuted,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
