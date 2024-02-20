package fr.medicapp.medicapp.ui.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import fr.medicapp.medicapp.database.AppDatabase
import fr.medicapp.medicapp.entity.DoctorEntity
import fr.medicapp.medicapp.model.Doctor
import fr.medicapp.medicapp.repository.DoctorRepository
import fr.medicapp.medicapp.ui.calendar.Calendar
import fr.medicapp.medicapp.ui.doctors.DoctorInfos
import fr.medicapp.medicapp.ui.doctors.DoctorsAdd
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
            val db = AppDatabase.getInstance(LocalContext.current)
            val repoDoc = DoctorRepository(db.doctorDAO())
            val docs: MutableList<Doctor> = remember {mutableListOf()}
            Thread {
                docs.clear()
                docs.addAll(repoDoc.getAll().map{it.toDoctor()}.filter { it.isValid() })
            }.start()
            DoctorsMainMenu(
                docs,
                onDoctorClick = {
                    navController.navigate(DoctorsRoute.Infos.route
                        .replace("{id}", it.id))
                },
                addDoctor = {
                    navController.navigate(DoctorsRoute.Add.route)
                }
            )
        }

        composable(route = DoctorsRoute.Add.route) {
            DoctorsAdd()
        }

        composable(route = DoctorsRoute.Infos.route) {
            val id = it.arguments?.getString("id") ?: return@composable
            val db = AppDatabase.getInstance(LocalContext.current)
            val repoDoc = DoctorRepository(db.doctorDAO())
            var doc = remember {Doctor("1", "", "")}
            Thread {
                val docteur = repoDoc.getOne(id).toDoctor()
                if (docteur.isValid()) {
                    doc = docteur
            }
            }.start()
            DoctorInfos()
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
    object Add : DoctorsRoute(route = "doctors_add")
    object Infos : DoctorsRoute(route = "doctors_infos/{id}")
}