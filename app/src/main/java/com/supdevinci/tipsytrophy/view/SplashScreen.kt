package com.supdevinci.tipsytrophy.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    val durationMillis = (3000..60000).random()
    println(durationMillis)
    val colorScheme = MaterialTheme.colorScheme

    // Animation de la barre de 0f à 1f
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // On lance l'animation de la barre sur 3 secondes
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis)
        )
        // Une fois fini, on redirige
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(colorScheme.background, colorScheme.surfaceVariant)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --- LOGO (Repris de LoginPage) ---
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
                tint = colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "TIPSY TROPHY",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 6.sp
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- BARRE DE CHARGEMENT ---
            Column(
                modifier = Modifier.width(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { progress.value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = colorScheme.primary,
                    trackColor = colorScheme.primary.copy(alpha = 0.1f),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Préparation de la soirée...",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }
    }
}