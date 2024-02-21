package fr.medicapp.medicapp.ui.navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
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
import fr.medicapp.medicapp.viewModel.SharedDoctorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                },
            )
        }

        composable(route = DoctorsRoute.Add.route) {
            val list = mutableListOf<Pair<Doctor, Int>>()

            DoctorsAdd(

                onCancel = {navController.navigate(DoctorsRoute.Main.route)},
            )
        }

        composable(route = DoctorsRoute.Infos.route) {
            val id = it.arguments?.getString("id") ?: return@composable
            val db = AppDatabase.getInstance(LocalContext.current)
            val repoDoc = DoctorRepository(db.doctorDAO())
            val viewModel = it.sharedViewModel<SharedDoctorViewModel>(navController = navController)
            val state by viewModel.sharedState.collectAsStateWithLifecycle()
            val context = LocalContext.current

           Thread{
               viewModel.getDoctor(id, repoDoc)
           }.start()

            DoctorInfos(
                state,
                onClose = {navController.navigate(DoctorsRoute.Main.route)},
                onClickMail = {
                    val intent = Intent(
                        Intent.ACTION_SENDTO,
                        Uri.parse("mailto:${it.email}"))
                    startActivity(context, intent, null)
                }
            )
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