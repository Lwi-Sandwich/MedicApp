package fr.medicapp.medicapp.ui.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import de.coldtea.smplr.smplralarm.smplrAlarmCancel
import de.coldtea.smplr.smplralarm.smplrAlarmUpdate
import fr.medicapp.medicapp.ai.PrescriptionAI
import fr.medicapp.medicapp.api.Apizza
import fr.medicapp.medicapp.database.AppDatabase
import fr.medicapp.medicapp.entity.MedicationEntity
import fr.medicapp.medicapp.model.Doctor
import fr.medicapp.medicapp.model.InfosMedication
import fr.medicapp.medicapp.model.Treatment
import fr.medicapp.medicapp.repository.InfosMedicationRepository
import fr.medicapp.medicapp.repository.MedicationRepository
import fr.medicapp.medicapp.repository.NotificationRepository
import fr.medicapp.medicapp.repository.SideEffectRepository
import fr.medicapp.medicapp.repository.TreatmentRepository
import fr.medicapp.medicapp.ui.prescription.EditPrescription
import fr.medicapp.medicapp.ui.prescription.Prescription
import fr.medicapp.medicapp.ui.prescription.PrescriptionMainMenu
import fr.medicapp.medicapp.viewModel.SharedAddPrescriptionViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Cette fonction construit le graphe de navigation pour les prescriptions.
 *
 * @param navController Le contrôleur de navigation.
 */
@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.prescriptionNavGraph(
    navController: NavHostController
) {

    /**
     * Définit la navigation pour le graphe de prescription.
     */
    navigation(
        route = Graph.PRESCRIPTION,
        startDestination = PrescriptionRoute.Main.route,
    ) {

        /**
         * Composable pour la route principale de prescription.
         */
        composable(route = PrescriptionRoute.Main.route) {
            val db = AppDatabase.getInstance(LocalContext.current)
            val repository = TreatmentRepository(db.treatmentDAO())
            val repositoryMedication = MedicationRepository(db.medicationDAO())

            var result: MutableList<Treatment> = mutableListOf()
            Thread {
                val treatments = repository.getAll().map { it.toTreatment(repositoryMedication) }

                result.clear()
                result.addAll(treatments)

                result.forEach {
                    Log.d("TAG", it.toString())
                }
            }.start()

            val ordonnance = remember {
                result
            }

            PrescriptionMainMenu(
                ordonnances = ordonnance,
                onPrescription = { id ->
                    navController.navigate(PrescriptionRoute.Prescription.route.replace("{id}", id))
                },
                addPrescription = {
                    navController.navigate(PrescriptionRoute.AddPrescription.route)
                },
            )
        }

        /**
         * Composable pour afficher une prescription spécifique.
         */
        composable(route = PrescriptionRoute.Prescription.route) {
            val id = it.arguments?.getString("id") ?: return@composable
            val context = LocalContext.current
            val db = AppDatabase.getInstance(LocalContext.current)
            val repository = TreatmentRepository(db.treatmentDAO())
            val repositoryMedication = MedicationRepository(db.medicationDAO())
            val repositorySideEffect = SideEffectRepository(db.sideEffectDAO())
            val repositoryNotification = NotificationRepository(db.notificationDAO())
            val repositoryInfos = InfosMedicationRepository(db.infosMedicationDAO())
            val apizza = Apizza.getInstance()

            val result: MutableList<Treatment> = mutableListOf()
            val resultInfos: MutableMap<String, InfosMedication> = mutableMapOf()

            fun onClickLien(lien: String) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(lien))
                startActivity(context, intent, null)
            }

            Thread {
                result.clear()
                val treatmentEntity = repository.getOne(id)
                val treatment = treatmentEntity.toTreatment(repositoryMedication)
                result.add(treatment)
                val cis = treatment.medication?.cisCode
                if (cis != null) {
                    val infos: InfosMedication = try{
                        InfosMedication.fromEntity(repositoryInfos.getOne(cis))
                    } catch (e: Exception) {
                        try {
                            val infosFetch = apizza.getInfosByCis(cis)
                            try{
                                repositoryInfos.add(infosFetch.toEntity())
                            } catch (_: Exception) { }
                            infosFetch
                        } catch (e: Exception) {
                            InfosMedication(
                                cisCode = cis,
                                indications_therapeutiques = "Indisponible",
                                classifcation_atc = "Indisponible",
                                principes_actifs = listOf("Indisponible"),
                                details = "Indisponible"
                            )
                        }
                    }
                    resultInfos[cis] = infos
                }
            }.start()

            val prescription = remember {
                result
            }

            val infos = remember {
                resultInfos
            }


            Prescription(
                consultation = prescription,
                informations = infos,
                onClose = {
                    navController.navigate(PrescriptionRoute.Main.route) {
                        popUpTo(PrescriptionRoute.Prescription.route) {
                            inclusive = true
                        }
                    }
                },
                onRemove = {
                    Thread {
                        prescription.map { treatment -> treatment?.toEntity() }
                            .forEach { treatment ->
                                if (treatment != null) {
                                    Log.d("TAG", "Deleting treatment: $treatment")

                                    val sideEffects =
                                        repositorySideEffect.getByMedicament(treatment.id)

                                    sideEffects.forEach { sideEffect ->
                                        repositorySideEffect.delete(sideEffect)
                                    }

                                    val notifications =
                                        repositoryNotification.getByMedicament(treatment.id)

                                    notifications.forEach { notification ->
                                        notification.alarms.forEach { alarm ->
                                            smplrAlarmCancel(context) {
                                                requestCode { alarm }
                                            }
                                        }

                                        repositoryNotification.delete(notification)
                                    }

                                    repository.delete(treatment)
                                }
                            }
                    }.start()
                    navController.navigate(PrescriptionRoute.Main.route) {
                        popUpTo(PrescriptionRoute.Prescription.route) {
                            inclusive = true
                        }
                    }
                },
                onUpdate = { treatmentId, notificationValue ->
                    Thread {
                        val treatment =
                            repository.getOne(treatmentId).toTreatment(repositoryMedication)

                        if (treatment != null) {
                            val notifications = repositoryNotification.getByMedicament(treatment.id)

                            Log.d("TAG", "Updating treatment: $notificationValue")

                            notifications.forEach { notification ->
                                notification.alarms.forEach { alarm ->
                                    smplrAlarmUpdate(context) {
                                        requestCode { alarm }
                                        isActive { notificationValue }
                                    }
                                }
                            }

                            treatment.notification = notificationValue
                            repository.update(treatment.toEntity())
                        }
                    }.start()
                },
                onClickLien = { lien ->
                    onClickLien(lien)
                }
            )
        }

        /**
         * Composable pour ajouter une nouvelle prescription.
         */
        composable(route = PrescriptionRoute.AddPrescription.route) {
            val viewModel =
                it.sharedViewModel<SharedAddPrescriptionViewModel>(navController = navController)
            val state by viewModel.sharedState.collectAsStateWithLifecycle()

            val db = AppDatabase.getInstance(LocalContext.current)
            val repository = TreatmentRepository(db.treatmentDAO())
            val repositoryMedication = MedicationRepository(db.medicationDAO())
            val repositoryInfos = InfosMedicationRepository(db.infosMedicationDAO())
            val apizza = Apizza.getInstance()

            var result: MutableList<MedicationEntity> = mutableListOf()

            Thread {
                result.clear()
                result.addAll(repositoryMedication.getAllWithoutNotTreadings())
            }.start()

            val medication = remember {
                result
            }

            val cameraPermissionState = rememberPermissionState(
                android.Manifest.permission.CAMERA
            )

            val context = LocalContext.current

            var hasImage by remember { mutableStateOf(false) }

            var imageUri by remember { mutableStateOf<Uri?>(null) }

            val loading = remember {
                mutableStateOf(false)
            }

            val alert = remember {
                mutableStateOf(false)
            }

            val alertMessage = remember {
                mutableStateOf("")
            }

            val imagePicker = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent(),
                onResult = { uri: Uri? ->
                    hasImage = uri != null
                    imageUri = uri

                    if (imageUri != null) {
                        loading.value = true
                        val prescriptionAI = PrescriptionAI.getInstance(context)
                        val prediction = prescriptionAI.analyse(
                            imageUri!!,
                            onPrediction = { prediction ->
                                var treatment = Treatment()
                                prediction.forEach { (word, label) ->
                                    when {
                                        label.startsWith("B-") -> {
                                            if (label.removePrefix("B-") == "Drug" && treatment.query.isNotEmpty()) {
                                                treatment.query = treatment.query.trim()
                                                treatment.posology = treatment.posology.trim()
                                                state.treatments.add(treatment)
                                                treatment = Treatment()
                                            }
                                            when (label.removePrefix("B-")) {
                                                "Drug" -> treatment.query += " $word"
                                                "DrugQuantity" -> treatment.posology += " $word"
                                                "DrugForm" -> treatment.posology += " $word"
                                                "DrugFrequency" -> treatment.posology += " $word"
                                                "DrugDuration" -> treatment.renew += " $word"
                                            }
                                        }

                                        label.startsWith("I-") -> {
                                            when (label.removePrefix("I-")) {
                                                "Drug" -> treatment.query += " $word"
                                                "DrugQuantity" -> treatment.posology += " $word"
                                                "DrugForm" -> treatment.posology += " $word"
                                                "DrugFrequency" -> treatment.posology += " $word"
                                                "DrugDuration" -> treatment.renew += " $word"
                                            }
                                        }
                                    }
                                }
                                if (treatment.query.isNotEmpty()) {
                                    treatment.query = treatment.query.trim()
                                    treatment.posology = treatment.posology.trim()
                                    state.treatments.add(treatment)
                                }
                            },
                            onDismiss = {
                                loading.value = false
                            }
                        )
                    }
                }
            )

            val cameraLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicture(),
                onResult = { success: Boolean ->
                    hasImage = success

                    if (imageUri != null && success) {
                        loading.value = true
                        val prescriptionAI = PrescriptionAI.getInstance(context)
                        val prediction = prescriptionAI.analyse(
                            imageUri!!,
                            onPrediction = { prediction ->
                                var treatment = Treatment()
                                prediction.forEach { (word, label) ->
                                    when {
                                        label.startsWith("B-") -> {
                                            if (label.removePrefix("B-") == "Drug" && treatment.query.isNotEmpty()) {
                                                treatment.query = treatment.query.trim()
                                                treatment.posology = treatment.posology.trim()
                                                state.treatments.add(treatment)
                                                treatment = Treatment()
                                            }
                                            when (label.removePrefix("B-")) {
                                                "Drug" -> treatment.query += " $word"
                                                "DrugQuantity" -> treatment.posology += " $word"
                                                "DrugForm" -> treatment.posology += " $word"
                                                "DrugFrequency" -> treatment.posology += " $word"
                                                "DrugDuration" -> treatment.renew += " $word"
                                            }
                                        }

                                        label.startsWith("I-") -> {
                                            when (label.removePrefix("I-")) {
                                                "Drug" -> treatment.query += " $word"
                                                "DrugQuantity" -> treatment.posology += " $word"
                                                "DrugForm" -> treatment.posology += " $word"
                                                "DrugFrequency" -> treatment.posology += " $word"
                                                "DrugDuration" -> treatment.renew += " $word"
                                            }
                                        }
                                    }
                                }
                                if (treatment.query.isNotEmpty()) {
                                    treatment.query = treatment.query.trim()
                                    treatment.posology = treatment.posology.trim()
                                    state.treatments.add(treatment)
                                }
                            },
                            onDismiss = {
                                loading.value = false
                            }
                        )
                    }
                }
            )

            if (loading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Traitement de l'ordonnance en cours...")

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator()
                    }
                }
            } else {
                EditPrescription(
                    prescription = state,
                    doctors = listOf(
                        Doctor(
                            id = "1",
                            lastName = "Doe",
                            firstName = "John",
                        ),
                        Doctor(
                            id = "2",
                            lastName = "Doe",
                            firstName = "Jane",
                        ),
                    ),
                    onCancel = {
                        navController.popBackStack()
                    },
                    onConfirm = {
                        Thread {
                            val treatments =
                                state.treatments.map { treatment -> treatment.toEntity() }
                            // Récupération des informations sur les médicaments du patient
                            val infos = state.treatments.mapNotNull { treatment ->
                                val cis = treatment.medication?.cisCode
                                if (cis != null) {
                                    val info: InfosMedication? = try {
                                        InfosMedication.fromEntity(repositoryInfos.getOne(cis))
                                    } catch (e: Exception) {
                                        try {
                                            val infosFetch = apizza.getInfosByCis(cis)
                                            try {
                                                repositoryInfos.add(infosFetch.toEntity())
                                            } catch (_: Exception) {
                                            }
                                            infosFetch
                                        } catch (e: Exception) {
                                            null
                                        }
                                    }
                                    info
                                } else {
                                    null
                                }
                            }
                            // Détecter si un médicament a un principe actif en commun avec un autre médicament
                            val principesActifsRedondants: MutableList<String> = mutableListOf()
                            val medicamentsConcernes: MutableList<String> = mutableListOf()
                            for (i in infos.indices) {
                                for (j in i + 1 until infos.size) {
                                    val info1 = infos[i]
                                    val info2 = infos[j]
                                    val intersection = info1.principes_actifs.intersect(info2.principes_actifs)
                                    if (intersection.isNotEmpty()) {
                                        principesActifsRedondants.addAll(intersection)
                                        medicamentsConcernes.add(info1.cisCode)
                                        medicamentsConcernes.add(info2.cisCode)
                                    }
                                }
                            }
                            val medicamentsConcernesUniques = medicamentsConcernes.distinct()
                            val principesActifsRedondantsUniques = principesActifsRedondants.distinct()
                            if (medicamentsConcernesUniques.isNotEmpty()) {
                                alertMessage.value = "Attention, les médicaments suivants ont des principes actifs en commun : " +
                                        medicamentsConcernesUniques.joinToString(", ") +
                                        ". Les principes actifs en commun sont : " +
                                        principesActifsRedondantsUniques.joinToString(", ") +
                                        "." +
                                        "Veuillez consulter un professionnel de santé pour plus d'informations."
                                alert.value = true
                            }
                        }.start()
                    },
                    onAlert = {
                        Thread {
                            val treatments = state.treatments.map { treatment -> treatment.toEntity() }
                            treatments.forEach { treatment ->
                                repository.add(treatment)
                            }
                        }.start()
                        if (state.treatments.any { it.notification }) {
                            navController.navigate(NotificationRoute.AddNotification.route) {
                                popUpTo(PrescriptionRoute.AddPrescription.route) {
                                    inclusive = true
                                }
                            }
                        } else {
                            navController.navigate(PrescriptionRoute.Main.route) {
                                popUpTo(PrescriptionRoute.AddPrescription.route) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    onCameraPicker = {
                        imageUri = context.createImageFile()
                        cameraLauncher.launch(imageUri)
                    },
                    cameraPermissionState = cameraPermissionState,
                    onCameraPermissionRequested = {
                        cameraPermissionState.launchPermissionRequest()
                    },
                    onImagePicker = {
                        imagePicker.launch("image/*")
                    },
                    medications = medication,
                    alertText = alertMessage.value,
                    alert = alert.value,
                    hideAlert = {alert.value = false}
                )
            }
        }
    }
}

/**
 * Crée un fichier image temporaire et retourne son Uri.
 *
 * @return L'Uri du fichier image créé.
 */
fun Context.createImageFile(): Uri {
    val provider: String = "${applicationContext.packageName}.fileprovider"
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"

    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        cacheDir
    ).apply {
        createNewFile()
    }

    return FileProvider.getUriForFile(applicationContext, provider, image)
}

/**
 * Récupère une instance partagée du ViewModel spécifié.
 * Cette fonction est utile pour partager des données entre plusieurs composables dans le même graphe de navigation.
 *
 * @param navController Le contrôleur de navigation.
 * @return Une instance partagée du ViewModel.
 */
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

/**
 * Cette classe scellée définit les différentes routes pour les prescriptions.
 */
sealed class PrescriptionRoute(val route: String) {
    /**
     * Route pour la page principale des prescriptions.
     */
    object Main : PrescriptionRoute(route = "mainmenu")

    /**
     * Route pour afficher une prescription spécifique.
     */
    object Prescription : PrescriptionRoute(route = "prescription/{id}")

    /**
     * Route pour ajouter une nouvelle prescription.
     */
    object AddPrescription : PrescriptionRoute(route = "add_prescriptions")
}