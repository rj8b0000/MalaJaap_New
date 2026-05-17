package com.rudraksh.naamjaap.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Session : Screen("session")
    object Settings : Screen("settings")
    object History : Screen("history")
    
    object SessionComplete : Screen("session_complete/{count}/{target}/{durationMillis}?mantra={mantra}") {
        fun createRoute(count: Int, target: Int, durationMillis: Long, mantra: String?): String {
            val base = "session_complete/$count/$target/$durationMillis"
            return if (mantra != null) "$base?mantra=$mantra" else base
        }
    }
}
