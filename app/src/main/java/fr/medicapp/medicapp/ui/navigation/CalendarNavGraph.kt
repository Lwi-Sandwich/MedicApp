package fr.medicapp.medicapp.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import fr.medicapp.medicapp.ui.calendar.Calendar

fun NavGraphBuilder.calendarNavGraph(navController: NavHostController) {
    /**
     * Définit la navigation pour le graphe du calendrier.
     */
    navigation(
        route = Graph.CALENDAR,
        startDestination = CalendarRoute.Main.route,
    ) {

        composable(route = CalendarRoute.Main.route) {
            Calendar()
        }
    }
}

/**
 * Cette classe représente une route du calendrier.
 * @property route La route sous forme de chaîne de caractères.
 */
sealed class CalendarRoute(val route: String) {
    /**
     * Représente la route principale du calendrier.
     */
    object Main : CalendarRoute(route = "calendar_main")
}