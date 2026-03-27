package com.supdevinci.tipsytrophy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.supdevinci.tipsytrophy.component.NavigationBar
import com.supdevinci.tipsytrophy.navigation.CocktailNavHost
import com.supdevinci.tipsytrophy.ui.theme.TipsyTrophyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipsyTrophyTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { NavigationBar(navController = navController) }
                ) { innerPadding ->
                    androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
                        CocktailNavHost(navController = navController)
                    }
                }
            }
        }
    }
}
