package fr.medicapp.medicapp.ui

import fr.medicapp.medicapp.entity.MedicationEntity
import fr.medicapp.medicapp.model.Duration
import fr.medicapp.medicapp.model.Notification
import fr.medicapp.medicapp.model.Treatment
import fr.medicapp.medicapp.ui.calendar.treatmentOfTheDay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class CalendarTest {
    @Test
    fun TestListeVide() {

        val treatments = emptyList<Treatment>()
        val notifications = emptyList<Notification>()
        val day = LocalDate.now()

        val result = treatmentOfTheDay(treatments, notifications, day)

        // Vérification des résultats
        assertTrue(result.isEmpty())
    }

    @Test
    fun TestDateAvant() {
        val notification1 = Notification(
            id = "1",
            medicationName = Treatment(
                id = "0",
                medication = MedicationEntity("","","", listOf(),"","","", LocalDate.now(),"","",
                    listOf(),false
                ),
                posology = "1 fois par jour",
                quantity = "20 comprimés",
                renew = "Automatique",
                duration = Duration(0, LocalDate.of(2020,10,10),LocalDate.of(2020,10,11)),
                notification = true
            ),
            frequency = mutableListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            hours = mutableListOf(8, 12),
            minutes = mutableListOf(0, 30),
            alarms = mutableListOf(1, 2)
        )
        val treatments = emptyList<Treatment>()
        val notifications = listOf(notification1)
        val day = LocalDate.of(2020,10,9)

        val result = treatmentOfTheDay(treatments, notifications, day)

        // Vérification des résultats
        assertTrue(result.isEmpty())
    }

    @Test
    fun TestDateApres() {
        val notification1 = Notification(
            id = "1",
            medicationName = Treatment(
                id = "0",
                medication = MedicationEntity("","","", listOf(),"","","", LocalDate.now(),"","",
                    listOf(),false
                ),
                posology = "1 fois par jour",
                quantity = "20 comprimés",
                renew = "Automatique",
                duration = Duration(0, LocalDate.of(2020,10,10),LocalDate.of(2020,10,11)),
                notification = true
            ),
            frequency = mutableListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            hours = mutableListOf(8, 12),
            minutes = mutableListOf(0, 30),
            alarms = mutableListOf(1, 2)
        )
        val treatments = emptyList<Treatment>()
        val notifications = listOf(notification1)
        val day = LocalDate.of(2020,10,12)

        val result = treatmentOfTheDay(treatments, notifications, day)

        // Vérification des résultats
        assertTrue(result.isEmpty())
    }

    @Test
    fun TestParJour() {
        val notification1 = Notification(
            id = "1",
            medicationName = Treatment(
                id = "0",
                medication = MedicationEntity("","test1","", listOf(),"","","", LocalDate.now(),"","",
                    listOf(),false
                ),
                posology = "1 fois par jour",
                quantity = "20 comprimés",
                renew = "Automatique",
                duration = Duration(0, LocalDate.of(2020,10,10),LocalDate.of(2020,10,11)),
                notification = true
            ),
            frequency = mutableListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            hours = mutableListOf(8, 12),
            minutes = mutableListOf(0, 30),
            alarms = mutableListOf(1, 2)
        )
        val notification2 = Notification(
            id = "1",
            medicationName = Treatment(
                id = "0",
                medication = MedicationEntity("","test2","", listOf(),"","","", LocalDate.now(),"","",
                    listOf(),false
                ),
                posology = "1 fois par jour",
                quantity = "20 comprimés",
                renew = "Automatique",
                duration = Duration(0, LocalDate.of(2020,10,10),LocalDate.of(2020,10,11)),
                notification = true
            ),
            frequency = mutableListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            hours = mutableListOf(8, 12),
            minutes = mutableListOf(0, 30),
            alarms = mutableListOf(1, 2)
        )
        val treatments = emptyList<Treatment>()
        val notifications = listOf(notification1, notification2)
        val day = LocalDate.of(2020,10,10)

        val result = treatmentOfTheDay(treatments, notifications, day)
        val expected = mutableMapOf(notification1.medicationName!!.medication!!.name to Pair(notification1.hours,notification1.minutes),
                notification2.medicationName!!.medication!!.name to Pair(notification2.hours,notification2.minutes))
        // Vérification des résultats
        assertEquals(expected,result)
    }

    @Test
    fun TestParSemaineContains() {
        val notification1 = Notification(
            id = "1",
            medicationName = Treatment(
                id = "0",
                medication = MedicationEntity("","test1","", listOf(),"","","", LocalDate.now(),"","",
                    listOf(),false
                ),
                posology = "1 fois par semaine",
                quantity = "20 comprimés",
                renew = "Automatique",
                duration = Duration(0, LocalDate.of(2020,10,10),LocalDate.of(2020,10,11)),
                notification = true
            ),
            frequency = mutableListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY),
            hours = mutableListOf(8, 12),
            minutes = mutableListOf(0, 30),
            alarms = mutableListOf(1, 2)
        )

        val treatments = emptyList<Treatment>()
        val notifications = listOf(notification1)
        val day = LocalDate.of(2020,10,10)

        val result = treatmentOfTheDay(treatments, notifications, day)
        val expected = mutableMapOf(notification1.medicationName!!.medication!!.name to Pair(notification1.hours,notification1.minutes))
        // Vérification des résultats
        assertEquals(expected,result)
    }

    @Test
    fun TestParSemaineNotContains() {
        val notification1 = Notification(
            id = "1",
            medicationName = Treatment(
                id = "0",
                medication = MedicationEntity("","test1","", listOf(),"","","", LocalDate.now(),"","",
                    listOf(),false
                ),
                posology = "1 fois par semaine",
                quantity = "20 comprimés",
                renew = "Automatique",
                duration = Duration(0, LocalDate.of(2020,10,10),LocalDate.of(2020,10,11)),
                notification = true
            ),
            frequency = mutableListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            hours = mutableListOf(8, 12),
            minutes = mutableListOf(0, 30),
            alarms = mutableListOf(1, 2)
        )

        val treatments = emptyList<Treatment>()
        val notifications = listOf(notification1)
        val day = LocalDate.of(2020,10,10)

        val result = treatmentOfTheDay(treatments, notifications, day)
        val expected = emptyMap<String,Pair<MutableList<Int>,MutableList<Int>>>()
        // Vérification des résultats
        assertEquals(expected,result)
    }

    @Test
    fun TestParMoisContains() {
        val notification1 = Notification(
            id = "1",
            medicationName = Treatment(
                id = "0",
                medication = MedicationEntity("","test1","", listOf(),"","","", LocalDate.now(),"","",
                    listOf(),false
                ),
                posology = "1 fois par mois",
                quantity = "20 comprimés",
                renew = "Automatique",
                duration = Duration(0, LocalDate.of(2020,10,10),LocalDate.of(2020,10,11)),
                notification = true
            ),
            frequency = mutableListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            hours = mutableListOf(8, 12),
            minutes = mutableListOf(0, 30),
            alarms = mutableListOf(1, 2)
        )

        val treatments = emptyList<Treatment>()
        val notifications = listOf(notification1)
        val day = LocalDate.of(2020,10,10)

        val result = treatmentOfTheDay(treatments, notifications, day)
        val expected = mutableMapOf(notification1.medicationName!!.medication!!.name to Pair(notification1.hours,notification1.minutes))
        // Vérification des résultats
        assertEquals(expected,result)
    }

    @Test
    fun TestParMoisNotContains() {
        val notification1 = Notification(
            id = "1",
            medicationName = Treatment(
                id = "0",
                medication = MedicationEntity("","test1","", listOf(),"","","", LocalDate.now(),"","",
                    listOf(),false
                ),
                posology = "1 fois par mois",
                quantity = "20 comprimés",
                renew = "Automatique",
                duration = Duration(0, LocalDate.of(2020,10,10),LocalDate.of(2020,10,11)),
                notification = true
            ),
            frequency = mutableListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            hours = mutableListOf(8, 12),
            minutes = mutableListOf(0, 30),
            alarms = mutableListOf(1, 2)
        )

        val treatments = emptyList<Treatment>()
        val notifications = listOf(notification1)
        val day = LocalDate.of(2020,11,10)

        val result = treatmentOfTheDay(treatments, notifications, day)
        val expected = emptyMap<String,Pair<MutableList<Int>,MutableList<Int>>>()
        // Vérification des résultats
        assertEquals(expected,result)
    }
}