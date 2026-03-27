package com.supdevinci.tipsytrophy.view.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.supdevinci.tipsytrophy.viewModel.LoginViewModel

@Composable
fun LoginPage(viewModel: LoginViewModel = viewModel(), navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    val colorScheme = MaterialTheme.colorScheme
    var isError by remember { mutableStateOf(false) }

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
                .padding(horizontal = 32.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --- SECTION LOGO ---
            Surface(
                modifier = Modifier.size(140.dp),
                shape = RoundedCornerShape(32.dp),
                color = colorScheme.surface.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Logo Tipsy Trophy",
                        modifier = Modifier.size(80.dp),
                        tint = colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tipsy Trophy",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Qui sera aussi alcoolique que mon père ?",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- FORMULAIRE ---
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it      // Première instruction
                    isError = false    // Deuxième instruction
                },
                label = { Text("Pseudo") },
                placeholder = { Text("Ex: Jean_Dujardin") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = colorScheme.primary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.5f),
                    focusedLabelColor = colorScheme.primary,
                    cursorColor = colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- BOUTON CONNEXION ---
            Button(
                onClick = {
                    viewModel.loginUser(username) { success ->
                        if (success) {
                            navController.navigate("profile")
                        } else {
                            isError = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp),
                enabled = username.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary,
                    disabledContainerColor = colorScheme.primary.copy(alpha = 0.3f),
                    disabledContentColor = colorScheme.onPrimary.copy(alpha = 0.5f)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    "SE CONNECTER",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.25.sp
                    )
                )
            }

            if(isError){
                Text("Utilisateur introuvable")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- LIEN INSCRIPTION ---
            TextButton(
                onClick = { navController.navigate("signup") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Pas encore de compte ?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "S'inscrire",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}
