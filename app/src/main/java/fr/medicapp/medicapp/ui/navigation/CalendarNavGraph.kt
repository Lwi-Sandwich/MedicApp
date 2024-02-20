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
import fr.medicapp.medicapp.repository.MedicationRepository
import fr.medicapp.medicapp.repository.NotificationRepository
import fr.medicapp.medicapp.repository.TreatmentRepository
import fr.medicapp.medicapp.ui.calendar.Calendar
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
            val db = AppDatabase.getInstance(LocalContext.current)
            val repositoryNotification = NotificationRepository(db.notificationDAO())
            val repositoryTreatment = TreatmentRepository(db.treatmentDAO())
            val repositoryMedication = MedicationRepository(db.medicationDAO())

            var result: MutableList<Notification> = mutableListOf()
            var resultDico = mutableMapOf<String, MutableList<Notification>>()
            Thread {
                val notificationEntityTmp = repositoryNotification.getAll()

                val notifications = notificationEntityTmp.map {
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
                }
            }.start()
            val notification = remember {
                resultDico
            }
            Calendar(notification)
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
