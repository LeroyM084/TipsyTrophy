package com.supdevinci.tipsytrophy.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.supdevinci.tipsytrophy.data.SessionManager
import com.supdevinci.tipsytrophy.viewModel.LeaderBoardViewModel
// ... tes autres imports
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material.icons.filled.Check
import kotlinx.coroutines.launch

@Composable
fun ProfilePage(navController : NavHostController, viewmodel : LeaderBoardViewModel = viewModel()) {
    val currentUser = SessionManager.currentUser
    val colorScheme = MaterialTheme.colorScheme
    var friendUsername by remember { mutableStateOf("") }

    // 1. Création du scope pour l'appel suspend
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(colorScheme.background, colorScheme.surfaceVariant)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // --- AVATAR / TROPHÉE ---
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = colorScheme.primary.copy(alpha = 0.1f),
                border = BorderStroke(2.dp, colorScheme.primary)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = currentUser?.name ?: "Champion",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            )

            Text(
                text = "Membre de la ligue Tipsy",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.primary.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- CARTE DES INFOS ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = colorScheme.surface.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    ProfileInfoRow(
                        icon = Icons.Default.Badge,
                        label = "Pseudo",
                        value = currentUser?.name ?: "Invité"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White.copy(0.1f))
                    ProfileInfoRow(
                        icon = Icons.Default.Transgender,
                        label = "Sexe",
                        value = if (currentUser?.sex == "M") "Homme" else "Femme"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White.copy(0.1f))
                    ProfileInfoRow(
                        icon = Icons.Default.MonitorWeight,
                        label = "Poids",
                        value = "${currentUser?.weight ?: 0} kg"
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp)) // Remplacement du weight(1f) car on est dans un scroll

            // --- SECTION AJOUT D'AMI ---
            Text(
                text = "AJOUTER UN AMI",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = friendUsername,
                    onValueChange = { friendUsername = it },
                    placeholder = { Text("Pseudo de l'ami") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = colorScheme.primary
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.5f)
                    )
                )

                // 2. Bouton d'action avec gestion de la coroutine
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                viewmodel.addFriend(friendUsername = friendUsername)
                                friendUsername = ""
                            } catch (e: Exception) {
                                // Gérer l'erreur si besoin
                            }
                        }
                    },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = friendUsername.isNotBlank(),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Ajouter"
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- BOUTON DÉCONNEXION ---
            Button(
                onClick = {
                    SessionManager.logout()
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE74C3C),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("SE DÉCONNECTER", fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. L'icône à gauche (Badge, Transgender ou MonitorWeight)
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 2. La colonne de texte à droite
        Column {
            // Le petit titre (ex: "Pseudo") en gris transparent
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.5f))
            // La valeur réelle (ex: "Jean_Dujardin") en blanc gras
            Text(value, style = MaterialTheme.typography.bodyLarge, color = Color.White, fontWeight = FontWeight.Medium)
        }
    }
}