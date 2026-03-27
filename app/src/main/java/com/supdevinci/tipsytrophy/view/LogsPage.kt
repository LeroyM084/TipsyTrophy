package com.supdevinci.tipsytrophy.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.supdevinci.tipsytrophy.component.LogItem
import com.supdevinci.tipsytrophy.viewModel.CoktailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsPage(
    viewModel: CoktailViewModel = viewModel(),
) {
    val logs = viewModel.userLogs
    viewModel.loadLogsByUserId()
    viewModel.calculateCurrentAlcoholLevel()

    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorScheme.background,
                        colorScheme.surfaceVariant
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // --- HEADER ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 32.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = RoundedCornerShape(28.dp),
                    color = colorScheme.surface.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.LocalBar,
                            contentDescription = null,
                            modifier = Modifier.size(52.dp),
                            tint = colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tipsy Trophy",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 4.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // --- BANNER TAUX D'ALCOOL ---
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = colorScheme.primary.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "Niveau d'alcool estimé : ${"%.2f".format(viewModel.currentAlcoholLevel)} g/L",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // --- CONTENU ---
            if (logs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Aucun cocktail enregistré.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            } else {
                Text(
                    text = "Historique des boissons",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 4.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(logs) { log ->
                        LogItem(log)
                    }
                }
            }
        }
    }
}

