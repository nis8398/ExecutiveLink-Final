package com.example.executivelink.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ExecutiveGold,
    onPrimary = ExecutiveDarkBg,
    primaryContainer = ExecutiveGoldDark,
    onPrimaryContainer = TextPrimary,
    secondary = ExecutiveCyan,
    onSecondary = ExecutiveDarkBg,
    secondaryContainer = ExecutiveCyanDark,
    onSecondaryContainer = TextPrimary,
    tertiary = ExecutiveEmerald,
    background = ExecutiveDarkBg,
    onBackground = TextPrimary,
    surface = ExecutiveSurface,
    onSurface = TextPrimary,
    surfaceVariant = ExecutiveCard,
    onSurfaceVariant = TextSecondary,
    outline = ExecutiveBorder
)

@Composable
fun ExecutiveLinkTheme(
    darkTheme: Boolean = true, // Default to sleek executive dark mode
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.surface.toArgb()
                window.navigationBarColor = colorScheme.surface.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
