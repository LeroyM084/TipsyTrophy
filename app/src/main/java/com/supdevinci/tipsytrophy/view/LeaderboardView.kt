package com.supdevinci.tipsytrophy.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.supdevinci.tipsytrophy.component.LeaderboardItem
import com.supdevinci.tipsytrophy.viewModel.LeaderBoardViewModel

@Composable
fun LeaderboardView(viewmodel: LeaderBoardViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewmodel.loadLeaderboard()
    }

    val leaderboardData = viewmodel.leaderboardData
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
            // --- HEADER SECTION ---
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "CLASSEMENT",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp
                )
            )

            Text(
                text = "De la semaine",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- LISTE ---
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 32.dp,
                    end = 32.dp,
                    top = 12.dp,
                    bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(leaderboardData) { data ->
                    LeaderboardItem(data)
                }
            }
        }
    }
}