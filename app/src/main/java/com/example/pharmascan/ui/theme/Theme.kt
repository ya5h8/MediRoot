package com.example.pharmascan

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF9100), // Orange for buttons
    onPrimary = Color.White,
    background = Color.Transparent, // We'll use a gradient
    surface = Color(0xFFFFE28C), // Card background
    onSurface = Color.Black,
    error = Color(0xFFFF3D00),
    onError = Color.White
)

@Composable
fun PharmaScanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(
            titleLarge = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color.White
            ),
            titleMedium = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color.Black
            ),
            bodyMedium = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            )
        )
    ) {
        content()
    }
}

fun Modifier.gradientBackground(): Modifier {
    return this.background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF66D9EF), // Light blue
                Color(0xFF1E90FF)  // Darker blue
            )
        )
    )
}