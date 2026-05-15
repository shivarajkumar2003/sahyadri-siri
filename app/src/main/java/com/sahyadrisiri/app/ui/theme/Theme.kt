package com.sahyadrisiri.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Brand colors matching the website
val Blue600 = Color(0xFF2563EB)
val Blue500 = Color(0xFF3B82F6)
val Blue100 = Color(0xFFDBEAFE)
val Blue50 = Color(0xFFEFF6FF)
val Blue200 = Color(0xFFBFDBFE)

val Red500 = Color(0xFFEF4444)
val Red100 = Color(0xFFFEE2E2)
val Red50 = Color(0xFFFEF2F2)

val Orange500 = Color(0xFFF97316)
val Orange100 = Color(0xFFFFEDD5)
val Orange50 = Color(0xFFFFF7ED)

val Slate900 = Color(0xFF0F172A)
val Slate800 = Color(0xFF1E293B)
val Slate700 = Color(0xFF334155)
val Slate500 = Color(0xFF64748B)
val Slate400 = Color(0xFF94A3B8)
val Slate200 = Color(0xFFE2E8F0)
val Slate100 = Color(0xFFF1F5F9)
val Slate50 = Color(0xFFF8FAFC)

val Gray900 = Color(0xFF111827)
val Gray700 = Color(0xFF374151)
val Gray600 = Color(0xFF4B5563)
val Gray500 = Color(0xFF6B7280)
val Gray400 = Color(0xFF9CA3AF)
val Gray300 = Color(0xFFD1D5DB)
val Gray200 = Color(0xFFE5E7EB)
val Gray100 = Color(0xFFF3F4F6)
val Gray50 = Color(0xFFF9FAFB)

val White = Color(0xFFFFFFFF)
val Background = Color(0xFFFDFDFD)

val Emerald50 = Color(0xFFECFDF5)
val Emerald500 = Color(0xFF10B981)

val AmberWarning = Color(0xFFF59E0B)
val Amber100 = Color(0xFFFEF3C7)

private val LightColorScheme = lightColorScheme(
    primary = Blue600,
    onPrimary = White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue600,
    secondary = Slate700,
    onSecondary = White,
    secondaryContainer = Slate100,
    onSecondaryContainer = Slate700,
    tertiary = Emerald500,
    background = Background,
    onBackground = Gray900,
    surface = White,
    onSurface = Gray900,
    surfaceVariant = Gray50,
    onSurfaceVariant = Gray600,
    outline = Gray200,
    error = Red500,
    onError = White,
    errorContainer = Red100,
    onErrorContainer = Red500,
)

@Composable
fun SahyadriSiriTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Using light theme only to match the website design
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
