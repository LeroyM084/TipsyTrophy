package com.supdevinci.tipsytrophy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

// Définition de tes couleurs personnalisées (Palette)
private val DeepRoyalBlue = Color(0xFF001233) // Fond très foncé, tire vers le bleu roi
private val RichGold = Color(0xFFF6C324)     // Jaune doré éclatant (Trophée)
private val DarkerGold = Color(0xFFBF9B30)   // Pour les variantes ou états pressés
private val OnBlueText = Color(0xFFECF0F1)   // Blanc cassé très léger pour le texte sur fond bleu
private val BlueSurface = Color(0xFF001A4A)  // Un bleu légèrement plus clair pour les cartes/surfaces

val defaultColorScheme = darkColorScheme(
    // --- COULEUR PRIMAIRE (Le Trophée / L'Or) ---
    primary = RichGold,
    onPrimary = Color.Black,
    primaryContainer = DarkerGold,
    onPrimaryContainer = Color.White,

    // --- COULEUR SECONDAIRE (Accents) ---
    secondary = Color(0xFFFFD700),
    onSecondary = Color.Black,

    // --- FOND ET SURFACES (Le Bleu Roi) ---
    background = DeepRoyalBlue,
    onBackground = OnBlueText,

    surface = BlueSurface,
    onSurface = OnBlueText,

    surfaceVariant = Color(0xFF01215A),
    onSurfaceVariant = Color(0xFFC4CBDC),

    error = Color(0xFFCF6679),
    onError = Color.Black,

    outline = Color(0xFF8A98B1)
)

@Composable
fun TipsyTrophyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Désactivé par défaut pour forcer ton thème personnalisé (DeepRoyalBlue)
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> defaultColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
