package fr.medicapp.medicapp.ui.navigation

import android.util.Log
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import fr.medicapp.medicapp.database.AppDatabase
import fr.medicapp.medicapp.model.Notification
import fr.medicapp.medicapp.model.Treatment
import fr.medicapp.medicapp.repository.MedicationRepository
import fr.medicapp.medicapp.repository.NotificationRepository
import fr.medicapp.medicapp.repository.TreatmentRepository
import fr.medicapp.medicapp.ui.calendar.Calendar
import fr.medicapp.medicapp.ui.prescription.PrescriptionMainMenu
import java.util.UUID

fun NavGraphBuilder.calendarNavGraph(navController: NavHostController) {
    /**
     * Définit la navigation pour le graphe du calendrier.
     */
    navigation(
        route = Graph.CALENDAR,
        startDestination = CalendarRoute.Main.route,
    ) {

        composable(route = CalendarRoute.Main.route) {
            /*val db = AppDatabase.getInstance(LocalContext.current)
            val repositoryTreatment = TreatmentRepository(db.treatmentDAO())

            var result: MutableList<Notification> = mutableListOf()
            var resultDico = mutableMapOf<String, MutableList<Notification>>()
            Thread {
                val treatmentEntityTmp = repositoryTreatment.getAll()

                /*val notifications = notificationEntityTmp.map {
                    val treatmentTmp = repositoryTreatment.getOne(it.medicationName)
                        .toTreatment(repositoryMedication)
                    val notificationTmp = it.toNotification()
                    notificationTmp.medicationName = treatmentTmp
                    notificationTmp
                }

                result.clear()
                result.addAll(notifications)

                resultDico = sortNotificationsByDayOfWeek(result)

                resultDico.forEach {
                    Log.d("TAG", it.toString())
                }*/
            }.start()
            val notification = remember {
                resultDico
            }
            Calendar(notification)*/

            val db = AppDatabase.getInstance(LocalContext.current)
            val repositoryTreatment = TreatmentRepository(db.treatmentDAO())
            val repositoryMedication = MedicationRepository(db.medicationDAO())
            val repositoryNotification = NotificationRepository(db.notificationDAO())

            var resultTreatment : MutableList<Treatment> = mutableListOf()
            var resultNotification : MutableList<Notification> = mutableListOf()
            Thread {
                val treatments = repositoryTreatment.getAll().map { it.toTreatment(repositoryMedication) }
                val notifications = repositoryNotification.getAll().map { it.toNotification(repositoryTreatment, repositoryMedication) }

                resultTreatment.clear()
                resultTreatment.addAll(treatments)

                resultNotification.clear()
                resultNotification.addAll(notifications)

                resultTreatment.forEach {
                    Log.d("TAG", it.toString())
                }
                resultNotification.forEach {
                    Log.d("TAG", it.toString())
                }
            }.start()

            val treatment = remember {
                resultTreatment
            }

            val notification = remember {
                resultNotification
            }

            Calendar(
                treatment,
                notification
            )
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

fun sortNotificationsByDayOfWeek(notifications: List<Notification>): MutableMap<String, MutableList<Notification>> {
    val notificationsByDayOfWeek = mutableMapOf<String, MutableList<Notification>>()

    notifications.forEach { notification ->
        notification.frequency.forEach { dayOfWeek ->
            val key = dayOfWeek.toString()
            val existingNotifications = notificationsByDayOfWeek.getOrDefault(key, mutableListOf())

            notification.hours.forEachIndexed { index, hour ->
                val minute = notification.minutes[index]
                val newNotification = Notification(
                    id = notification.id,
                    medicationName = notification.medicationName,
                    frequency = mutableListOf(dayOfWeek),
                    hours = mutableListOf(hour),
                    minutes = mutableListOf(minute)
                )
                existingNotifications.add(newNotification)
            }

            notificationsByDayOfWeek[key] = existingNotifications
        }
    }

    return notificationsByDayOfWeek
}
