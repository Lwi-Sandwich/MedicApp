package fr.medicapp.medicapp.ui.navigation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import fr.medicapp.medicapp.api.Apizza
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
            val api = Apizza.getInstance()
            val viewModel = it.sharedViewModel<SharedDoctorViewModel>(navController = navController)
            val state by viewModel.sharedStateList.collectAsStateWithLifecycle()
            val db = AppDatabase.getInstance(LocalContext.current)
            val repoDoc = DoctorRepository(db.doctorDAO())
            val context = LocalContext.current
            val locationPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            val locationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    permissions.values.reduce { acc, isPermissionGranted ->
                        acc && isPermissionGranted
                    }
                })

            DoctorsAdd(
                doctorsList = state,
                onSearch = {nom, tri ->
                    Thread {
                       viewModel.getDoctorsByName(api, nom, tri, context)
                    }.start()
                },
                onCancel = {navController.navigate(DoctorsRoute.Main.route)},
                onConfirm = {
                    Thread {
                        try {
                            repoDoc.add(
                                DoctorEntity(
                                    it.id,
                                    it.lastName,
                                    it.firstName,
                                    it.phoneNumber,
                                    it.email,
                                    it.specialty,
                                    it.zipCode,
                                    it.city,
                                    it.address
                                )
                            )
                        } catch (_: Exception) { }
                    }.start()
                    navController.navigate(DoctorsRoute.Main.route)
                },
                onCheckDistance = {
                    if (it) {
                        val permission = Manifest.permission.ACCESS_FINE_LOCATION
                        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionLauncher.launch(locationPermissions)
                        }
                    }

                }
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
                },
                onDelete = {
                    Thread {
                        try {
                            repoDoc.delete(DoctorEntity(
                                it.id,
                                it.lastName,
                                it.firstName,
                                it.phoneNumber,
                                it.email,
                                it.specialty,
                                it.zipCode,
                                it.city,
                                it.address))
                        } catch (_: Exception) { }
                        MainScope().launch {
                            navController.navigate(DoctorsRoute.Main.route)
                        }
                    }.start()
                },
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