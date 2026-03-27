package com.supdevinci.tipsytrophy.view

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavHostController) {
    // Durée aléatoire (restreinte ici à un range raisonnable pour l'expérience utilisateur)
    val durationMillis = remember { (7000..60000).random() }
    val colorScheme = MaterialTheme.colorScheme

    // Animation de la barre
    val progress = remember { Animatable(0f) }

    // Gestion des textes qui défilent
    val loadingTexts = listOf(
        "A la recherche du daron d'Anthony...",
        "'Hmmm des donuts' - Thomas",
        "Ilona elle est grave mature pour son âge...",
        "Calvitie de Théo : prête",
        "L'app fait 3Go à cause d'une photo de Manu",
        "'xDD *insert random gif*' - Crash Bandicoot",
        "Le saviez-vous : la capacité à tenir l'alcool réside dans le nombre de cheuveux de la personne",
        "'Are u a boy or a girl' - Elowan & Anthony, Juin 2026, Bali"
    )
    var currentTextIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        // Lancement de l'animation de progression
        launch {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis)
            )
            // Une fois fini, on redirige
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }

        // Boucle pour faire défiler les textes
        launch {
            while (progress.value < 1f) {
                delay(1500) // Change de texte toutes les 1.5s
                currentTextIndex = (currentTextIndex + 1) % loadingTexts.size
            }
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

            Column(
                modifier = Modifier.width(250.dp), // Un peu plus large pour les textes longs
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

                Spacer(modifier = Modifier.height(16.dp))

                // Animation de transition fluide entre les textes
                AnimatedContent(
                    targetState = loadingTexts[currentTextIndex],
                    transitionSpec = {
                        fadeIn(animationSpec = tween(1000)) togetherWith
                                fadeOut(animationSpec = tween(500))
                    },
                    label = "TextAnimation"
                ) { targetText ->
                    Text(
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        text = targetText,
                        style = MaterialTheme.typography.labelMedium,
                        color = colorScheme.onBackground.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}