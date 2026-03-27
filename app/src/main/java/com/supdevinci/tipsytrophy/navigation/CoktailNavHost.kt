package com.supdevinci.tipsytrophy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.supdevinci.tipsytrophy.view.AddCocktail
import com.supdevinci.tipsytrophy.view.LeaderboardView
import com.supdevinci.tipsytrophy.view.auth.LoginPage
import com.supdevinci.tipsytrophy.view.LogsPage
import com.supdevinci.tipsytrophy.view.ProfilePage
import com.supdevinci.tipsytrophy.view.auth.SignUpPage
import com.supdevinci.tipsytrophy.view.SplashScreen

object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"

    const val ADD_COCKTAIL = "add_cocktail"
    const val PROFILE = "profile"
    const val LOGS = "logs"
    const val LEADERBOARD = "leaderboard"
    const val SPLASH = "splash"
}



@Composable
fun CocktailNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(navController = navController)
        }
        composable(Routes.LOGIN) {
            LoginPage(navController = navController)
        }
        composable(Routes.SIGNUP) {
            SignUpPage(navController = navController)
        }

        composable(Routes.PROFILE) {
            ProfilePage(navController = navController)
        }
        composable(Routes.ADD_COCKTAIL) {
            AddCocktail()
        }
        composable(Routes.LOGS) {
            LogsPage()
        }
        composable(Routes.LEADERBOARD) {
            LeaderboardView()
        }
    }
}
