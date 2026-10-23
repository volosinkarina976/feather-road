package com.BzNpXqL.vTmJrK.presentation.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.BzNpXqL.vTmJrK.R
import com.BzNpXqL.vTmJrK.core.theme.AccentGold
import com.BzNpXqL.vTmJrK.core.theme.AccentTerracotta
import com.BzNpXqL.vTmJrK.core.theme.OnAccent
import com.BzNpXqL.vTmJrK.core.theme.SurfaceCard
import com.BzNpXqL.vTmJrK.core.theme.SurfaceRaised
import com.BzNpXqL.vTmJrK.core.theme.TextMuted
import com.BzNpXqL.vTmJrK.core.theme.TextPrimary
import com.BzNpXqL.vTmJrK.core.ui.SecondaryButton

@Composable
fun SettingsDialog(
    soundEnabled: Boolean,
    hapticsEnabled: Boolean,
    resetArmed: Boolean,
    onToggleSound: (Boolean) -> Unit,
    onToggleHaptics: (Boolean) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    val onLabel = stringResource(R.string.sd_on)
    val offLabel = stringResource(R.string.sd_off)
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
                text = stringResource(R.string.dialog_settings),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(18.dp))
            SettingRow(
                label = stringResource(R.string.setting_sound),
                checked = soundEnabled,
                stateLabel = if (soundEnabled) onLabel else offLabel,
                onChange = onToggleSound
            )
            Spacer(modifier = Modifier.height(10.dp))
            SettingRow(
                label = stringResource(R.string.setting_haptics),
                checked = hapticsEnabled,
                stateLabel = if (hapticsEnabled) onLabel else offLabel,
                onChange = onToggleHaptics
            )
            Spacer(modifier = Modifier.height(20.dp))
            SecondaryButton(
                label = stringResource(
                    if (resetArmed) R.string.cta_confirm_reset else R.string.cta_reset_progress
                ),
                onClick = onReset,
                accent = AccentTerracotta,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                label = stringResource(R.string.cta_close),
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SettingRow(
    label: String,
    checked: Boolean,
    stateLabel: String,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceCard.copy(alpha = 0.85f))
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .semantics { stateDescription = stateLabel },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = OnAccent,
                checkedTrackColor = AccentGold,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = SurfaceRaised
            )
        )
    }
}
