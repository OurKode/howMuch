package com.example.howmuch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Color definitions matching the /minimalist-ui guidelines
object MinimalistColors {
    val CanvasBg = Color(0xFFFBFBFA) // Warm Off-White / Warm Bone
    val CardBg = Color(0xFFFFFFFF) // Pure White
    val BorderColor = Color(0xFFEAEAEA) // Structural Divider Color
    
    val TextPrimary = Color(0xFF111111) // Off-black / Charcoal
    val TextSecondary = Color(0xFF787774) // Muted Gray
    
    // Muted Pastels for accents
    val PaleGreenBg = Color(0xFFEDF3EC)
    val PaleGreenText = Color(0xFF346538)
    
    val PaleRedBg = Color(0xFFFDEBEC)
    val PaleRedText = Color(0xFF9F2F2D)
    
    val PaleBlueBg = Color(0xFFE1F3FE)
    val PaleBlueText = Color(0xFF1F6C9F)
    
    val PaleYellowBg = Color(0xFFFBF3DB)
    val PaleYellowText = Color(0xFF956400)
}

// Typography configuration using system clean Sans-Serif & Monospace
val MinimalistTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.02).sp,
        color = MinimalistColors.TextPrimary
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.02).sp,
        color = MinimalistColors.TextPrimary
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        color = MinimalistColors.TextPrimary
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        color = MinimalistColors.TextPrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = MinimalistColors.TextSecondary
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.05.sp,
        color = MinimalistColors.TextSecondary
    )
)

private val LightColorScheme = lightColorScheme(
    primary = MinimalistColors.TextPrimary,
    onPrimary = Color.White,
    background = MinimalistColors.CanvasBg,
    onBackground = MinimalistColors.TextPrimary,
    surface = MinimalistColors.CardBg,
    onSurface = MinimalistColors.TextPrimary,
    outline = MinimalistColors.BorderColor
)

/**
 * Custom application theme enforcing Utilitarian Minimalist UI styling rules.
 * Focuses on off-white canvas, clean dark charcoal text, monospace figures, and desaturated pastels.
 */
@Composable
fun HowMuchTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = MinimalistTypography,
        content = content
    )
}
