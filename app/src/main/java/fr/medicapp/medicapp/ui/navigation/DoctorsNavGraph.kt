package fr.medicapp.medicapp.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import fr.medicapp.medicapp.ui.calendar.Calendar
import fr.medicapp.medicapp.ui.doctors.DoctorsMainMenu

fun NavGraphBuilder.doctorsNavGraph(navController: NavHostController) {
    /**
     * Définit la navigation pour le graphe des docteurs.
     */
    navigation(
        route = Graph.DOCTORS,
        startDestination = DoctorsRoute.Main.route,
    ) {

        composable(route = DoctorsRoute.Main.route) {
            // Faire les manipulations de base de données pour
            // retrouver tous les docteurs enregistrés
            //TODO
            DoctorsMainMenu(mutableListOf())
        }
    }
}

/**
 * Cette classe représente une route des docteurs.
 * @property route La route sous forme de chaîne de caractères.
 */
sealed class DoctorsRoute(val route: String) {
    /**
     * Représente la route principale des docteurs.
     */
    object Main : DoctorsRoute(route = "doctors_main")
}