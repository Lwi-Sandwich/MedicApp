package fr.medicapp.medicapp.ui.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.medicapp.medicapp.database.AppDatabase
import fr.medicapp.medicapp.model.Notification
import fr.medicapp.medicapp.model.Treatment
import fr.medicapp.medicapp.repository.MedicationRepository
import fr.medicapp.medicapp.repository.NotificationRepository
import fr.medicapp.medicapp.repository.TreatmentRepository
import fr.medicapp.medicapp.ui.home.HomeScreen
import fr.medicapp.medicapp.ui.home.NavigationDrawerRoute

/**
 * Ceci est une classe de navigation pour l'écran d'accueil.
 * Elle contient les routes pour les différents écrans de l'application.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNavGraph(navController: NavHostController) {

    /**
     * Construit le graphe de navigation pour l'écran d'accueil.
     *
     * @param navController Le contrôleur de navigation.
     */
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = NavigationDrawerRoute.Home.route
    ) {

        /**
         * Composable pour l'écran d'accueil.
         */
        composable(route = NavigationDrawerRoute.Home.route) {
            val db = AppDatabase.getInstance(LocalContext.current)
            val repositoryTreatment = TreatmentRepository(db.treatmentDAO())
            val repositoryMedication = MedicationRepository(db.medicationDAO())
            val repositoryNotification = NotificationRepository(db.notificationDAO())

            var resultNotification : MutableList<Notification> = mutableListOf()
            Thread {
                val treatments = repositoryTreatment.getAll().map { it.toTreatment(repositoryMedication) }
                val notifications = repositoryNotification.getAll().map { it.toNotification(repositoryTreatment, repositoryMedication) }

                resultNotification.clear()
                resultNotification.addAll(notifications)

                resultNotification.forEach {
                    Log.d("TAG", it.toString())
                }
            }.start()

            val notification = remember {
                resultNotification
            }

            HomeScreen(
                notifications = notification,
                onAddPrescriptionClick = {
                    navController.navigate(PrescriptionRoute.AddPrescription.route)
                },
                onAddSideEffectClick = {
                    navController.navigate(SideEffectRoute.AddSideEffect.route)
                },
                onAddNotification = {
                    navController.navigate(NotificationRoute.AddNotification.route)
                }
            )
        }

        /**
         * Appelle la fonction prescriptionNavGraph pour construire le graphe de navigation des prescriptions.
         */
        prescriptionNavGraph(navController)

        /**
         * Appelle la fonction calendarNavGraph pour construire le graphe de navigation du calendrier.
         */
        calendarNavGraph(navController)

        /**
         * Appelle la fonction doctorsNavGraph pour construire le graphe de navigation des docteurs.
         */
        doctorsNavGraph(navController)

        /**
         * Appelle la fonction sideEffectNavGraph pour construire le graphe de navigation des effets indésirables.
         */
        sideEffectNavGraph(navController)

        /**
         * Appelle la fonction notificationNavGraph pour construire le graphe de navigation des notifications.
         */
        notificationNavGraph(navController)
    }
}
