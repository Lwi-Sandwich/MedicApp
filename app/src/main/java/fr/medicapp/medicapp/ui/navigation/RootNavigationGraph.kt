package fr.medicapp.medicapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.medicapp.medicapp.ui.home.NavigationDrawerScreen

/**
 * Cette fonction construit le graphe de navigation racine.
 *
 * @param navController Le contrôleur de navigation.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavGraph(navController: NavHostController) {

    /**
     * Définit le hôte de navigation pour le graphe de navigation racine.
     */
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.HOME
    ) {

        /**
         * Appelle la fonction authNavGraph pour construire le graphe de navigation d'authentification.
         */
        authNavGraph(navController = navController)

        /**
         * Composable pour l'écran d'accueil.
         */
        composable(route = Graph.HOME) {
            NavigationDrawerScreen()
        }
    }
}

/**
 * Cette classe scellée définit les différentes routes pour le graphe de navigation racine.
 */
object Graph {
    /**
     * Route pour le graphe de navigation racine.
     */
    const val ROOT = "root_graph"

    /**
     * Route pour le graphe de navigation d'authentification.
     */
    const val AUTHENTICATION = "auth_graph"

    /**
     * Route pour l'écran d'accueil.
     */
    const val HOME = "home_graph"

    /**
     * Route pour l'écran du calendrier.
     */
    const val CALENDAR = "calendar_graph"

    /**
     * Route pour le graphe de navigation des prescriptions.
     */
    const val PRESCRIPTION = "prescription_graph"

    /**
     * Route pour l'écran des doctors.
     */
    const val DOCTORS = "doctors_graph"

    /**
     * Route pour le graphe de navigation des effets indésirables.
     */
    const val SIDE_EFFECTS = "add_side_effects_graph"

    /**
     * Route pour le graphe de navigation des notifications.
     */
    const val NOTIFICATION = "notification_graph"
}