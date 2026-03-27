package com.supdevinci.tipsytrophy.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supdevinci.tipsytrophy.model.LeaderboardEntry

@Composable
fun LeaderboardItem(data: LeaderboardEntry) {
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        color = colorScheme.surface.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.2f)),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.width(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#${data.position}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                )
            }

            Text(
                text = data.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface
                )
            )

            Surface(
                color = colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "${data.drinkCount} 🏆",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = colorScheme.primary
                    )
                )
            }
        }
    }
}