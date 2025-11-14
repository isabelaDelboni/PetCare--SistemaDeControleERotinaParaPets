package com.example.petcaresistemadecontroleerotinaparapets.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Configuração do Modo Escuro
private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimary,      
    onPrimary = White,
    secondary = BlueSecondary,
    onSecondary = White,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = White
)

// Configuração do Modo Claro (O padrão)
private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = White,
    secondary = BlueSecondary,
    onSecondary = White,
    tertiary = OrangeLight,

    background = BackgroundCream,  
    surface = SurfaceWhite,       

    onBackground = Black,
    onSurface = Black
)

@Composable
fun PetCareSistemaDeControleERotinaParaPetsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}