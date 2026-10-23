package com.BzNpXqL.vTmJrK.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FeatherRoadColors = darkColorScheme(
    primary = AccentGold,
    onPrimary = OnAccent,
    primaryContainer = AccentGoldDeep,
    onPrimaryContainer = OnAccent,
    secondary = AccentTerracotta,
    onSecondary = TextPrimary,
    tertiary = AccentCyan,
    onTertiary = OnAccent,
    background = BgBase,
    onBackground = TextPrimary,
    surface = SurfaceRaised,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = BrassDark,
    outlineVariant = SignPlate,
    error = AccentTerracotta,
    onError = TextPrimary
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FeatherRoadColors,
        typography = AppTypography,
        content = content
    )
}
