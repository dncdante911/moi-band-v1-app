package com.moi.band.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dark Theme Colors (Metal/Rock inspired)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE91E63), // Pink/Red
    onPrimary = Color.White,
    primaryContainer = Color(0xFF880E4F),
    onPrimaryContainer = Color(0xFFFFD9E4),
    
    secondary = Color(0xFF9C27B0), // Purple
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4A148C),
    onSecondaryContainer = Color(0xFFF3E5F5),
    
    tertiary = Color(0xFFFF5722), // Deep Orange
    onTertiary = Color.White,
    
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFBDBDBD),
    
    error = Color(0xFFCF6679),
    onError = Color.Black
)

// Light Theme Colors
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFD81B60),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD9E4),
    onPrimaryContainer = Color(0xFF3B0020),
    
    secondary = Color(0xFF8E24AA),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3E5F5),
    onSecondaryContainer = Color(0xFF4A148C),
    
    tertiary = Color(0xFFFF5722),
    onTertiary = Color.White,
    
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1C1B1F),
    
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF49454F),
    
    error = Color(0xFFB00020),
    onError = Color.White
)

// Gothic Theme (darker, more dramatic)
private val GothicColorScheme = darkColorScheme(
    primary = Color(0xFF8B0000), // Dark Red
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4B0000),
    onPrimaryContainer = Color(0xFFFFB3B3),
    
    secondary = Color(0xFF4A148C), // Deep Purple
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1A0033),
    onSecondaryContainer = Color(0xFFE1BEE7),
    
    tertiary = Color(0xFF212121), // Almost Black
    onTertiary = Color(0xFFB0B0B0),
    
    background = Color(0xFF0A0A0A),
    onBackground = Color(0xFFC0C0C0),
    
    surface = Color(0xFF151515),
    onSurface = Color(0xFFC0C0C0),
    surfaceVariant = Color(0xFF1F1F1F),
    onSurfaceVariant = Color(0xFF9E9E9E),
    
    error = Color(0xFFFF0000),
    onError = Color.Black
)

// Punk Theme (vibrant, rebellious)
private val PunkColorScheme = darkColorScheme(
    primary = Color(0xFFFF1744), // Bright Red
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB71C1C),
    onPrimaryContainer = Color(0xFFFFCDD2),
    
    secondary = Color(0xFF00E676), // Neon Green
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF00C853),
    onSecondaryContainer = Color(0xFFB9F6CA),
    
    tertiary = Color(0xFFFFD600), // Bright Yellow
    onTertiary = Color.Black,
    
    background = Color(0xFF1A1A1A),
    onBackground = Color(0xFFF5F5F5),
    
    surface = Color(0xFF212121),
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF2E2E2E),
    onSurfaceVariant = Color(0xFFEEEEEE),
    
    error = Color(0xFFFF5252),
    onError = Color.Black
)

@Composable
fun MoiBandTheme(
    theme: String = "dark", // dark, light, gothic, punk
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        "light" -> LightColorScheme
        "gothic" -> GothicColorScheme
        "punk" -> PunkColorScheme
        else -> DarkColorScheme // default dark
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}