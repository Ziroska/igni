package ru.igni.manager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val IgniDarkColors = darkColorScheme(
    primary = Color(0xFFD8B36A),
    onPrimary = Color(0xFF201A10),
    secondary = Color(0xFF9FA9B3),
    background = Color(0xFF101214),
    surface = Color(0xFF171A1D),
    surfaceVariant = Color(0xFF22262A),
    onBackground = Color(0xFFF3F4F5),
    onSurface = Color(0xFFF3F4F5),
    outline = Color(0xFF3B4147)
)

@Composable
fun IgniTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = IgniDarkColors,
        typography = IgniTypography,
        content = content
    )
}
