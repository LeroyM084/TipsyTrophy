package com.supdevinci.tipsytrophy.component

import android.text.format.DateFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.supdevinci.tipsytrophy.data.local.entities.DrinkLogs

@Composable
fun LogItem(log: DrinkLogs) {
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = colorScheme.surface.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.3f))
    ) {
        ListItem(
            modifier = Modifier.padding(vertical = 4.dp),
            headlineContent = {
                Text(
                    text = log.label,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface
                )
            },
            supportingContent = {
                Text(
                    text = "Taux d'alcool : ${log.abv}%",
                    color = colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            overlineContent = {
                Text(
                    text = "Volume : ${log.size.toInt()}mL",
                    color = colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            trailingContent = {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = DateFormat.format("dd/MM", log.createdAt).toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            },
            leadingContent = {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalBar,
                        contentDescription = null,
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                    )
                }
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
    }
}