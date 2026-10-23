package com.BzNpXqL.vTmJrK.presentation.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.BzNpXqL.vTmJrK.R
import com.BzNpXqL.vTmJrK.core.theme.AccentGold
import com.BzNpXqL.vTmJrK.core.theme.NumericStyle
import com.BzNpXqL.vTmJrK.core.theme.SurfaceCard
import com.BzNpXqL.vTmJrK.core.theme.SurfaceRaised
import com.BzNpXqL.vTmJrK.core.theme.TextMuted
import com.BzNpXqL.vTmJrK.core.theme.TextPrimary
import com.BzNpXqL.vTmJrK.core.ui.SecondaryButton
import com.BzNpXqL.vTmJrK.core.ui.StarRow
import com.BzNpXqL.vTmJrK.domain.model.SchemeProgress

@Composable
fun LevelMapDialog(
    schemes: List<SchemeProgress>,
    current: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(SurfaceRaised, SurfaceCard)
                    )
                )
                .border(1.dp, AccentGold.copy(alpha = 0.30f), RoundedCornerShape(24.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.dialog_level_map),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            for (rowIndex in 0 until 4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (colIndex in 0 until 3) {
                        val position = rowIndex * 3 + colIndex
                        key(position) {
                            val entry = schemes.getOrNull(position)
                            if (entry == null) {
                                Spacer(modifier = Modifier.weight(1f))
                            } else {
                                SchemeTile(
                                    entry = entry,
                                    selected = entry.index == current,
                                    onSelect = onSelect,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            SecondaryButton(
                label = stringResource(R.string.cta_close),
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SchemeTile(
    entry: SchemeProgress,
    selected: Boolean,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    val lockedLabel = stringResource(R.string.cd_locked)
    Column(
        modifier = modifier
            .aspectRatio(0.86f)
            .clip(shape)
            .background(SurfaceCard.copy(alpha = if (entry.unlocked) 0.95f else 0.55f))
            .border(
                if (selected) 2.dp else 1.dp,
                AccentGold.copy(alpha = if (selected) 0.85f else 0.25f),
                shape
            )
            .clickable(enabled = entry.unlocked) { onSelect(entry.index) }
            .alpha(if (entry.unlocked) 1f else 0.35f)
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (entry.unlocked) {
            Text(
                text = (entry.index + 1).toString(),
                style = NumericStyle,
                fontSize = 20.sp,
                color = AccentGold
            )
            Text(
                text = stringResource(R.string.value_grid, entry.cols, entry.rows),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(5.dp))
            StarRow(
                earned = entry.stars,
                total = 3,
                description = stringResource(R.string.cd_stars, entry.stars, 3),
                starSize = 11.dp,
                gap = 3.dp
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier
                    .size(22.dp)
                    .clearAndSetSemantics { contentDescription = lockedLabel }
            )
        }
    }
}
