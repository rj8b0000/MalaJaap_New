package com.rudraksh.naamjaap.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rudraksh.naamjaap.presentation.history.HistoryScreen
import com.rudraksh.naamjaap.presentation.home.HomeScreen
import com.rudraksh.naamjaap.presentation.session.SessionCompleteScreen
import com.rudraksh.naamjaap.presentation.session.SessionScreen
import com.rudraksh.naamjaap.presentation.settings.SettingsScreen

@Composable
fun NaamJaapNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSession = { navController.navigate(Screen.Session.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        
        composable(Screen.Session.route) {
            SessionScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSummary = { count, target, duration, mantra ->
                    // Pop Session screen so we don't go back to it when pressing back from summary
                    navController.popBackStack()
                    navController.navigate(Screen.SessionComplete.createRoute(count, target, duration, mantra))
                }
            )
        }
        
        composable(
            route = Screen.SessionComplete.route,
            arguments = listOf(
                navArgument("count") { type = NavType.IntType },
                navArgument("target") { type = NavType.IntType },
                navArgument("durationMillis") { type = NavType.LongType },
                navArgument("mantra") { 
                    type = NavType.StringType
                    nullable = true 
                    defaultValue = null 
                }
            )
        ) { backStackEntry ->
            val count = backStackEntry.arguments?.getInt("count") ?: 0
            val target = backStackEntry.arguments?.getInt("target") ?: 108
            val durationMillis = backStackEntry.arguments?.getLong("durationMillis") ?: 0L
            val mantra = backStackEntry.arguments?.getString("mantra")
            
            SessionCompleteScreen(
                count = count,
                target = target,
                durationMillis = durationMillis,
                mantra = mantra,
                onDoneClick = { 
                    // Go back to home, clearing the backstack
                    navController.popBackStack(Screen.Home.route, inclusive = false) 
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.History.route) {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
