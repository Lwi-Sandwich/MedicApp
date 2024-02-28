package fr.medicapp.medicapp.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import fr.medicapp.medicapp.model.Notification
import fr.medicapp.medicapp.model.Treatment
import fr.medicapp.medicapp.ui.calendar.assets.Day
import fr.medicapp.medicapp.ui.calendar.assets.MedicationCalendarCard
import fr.medicapp.medicapp.ui.theme.EUGreen100
import fr.medicapp.medicapp.ui.theme.EUGreen120
import fr.medicapp.medicapp.ui.theme.EUGreen20
import fr.medicapp.medicapp.ui.theme.EUGreen80
import fr.medicapp.medicapp.ui.theme.EUYellow110
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(
    treatments : MutableList<Treatment>,
    notifications : MutableList<Notification>
) {
    val darkmode: Boolean = isSystemInDarkTheme()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Unspecified,
                    titleContentColor = if (darkmode) Color.White else Color.Black,
                ),
                title = {
                    Text(
                        "Calendrier des prises",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(10.dp)
        ) {
            val currentDate = remember { LocalDate.now() }
            val currentMonth = remember { YearMonth.now() }
            val startDate = remember { currentMonth.minusMonths(100).atStartOfMonth() }
            val endDate = remember { currentMonth.plusMonths(100).atEndOfMonth() }
            val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library
            var selection by remember { mutableStateOf(currentDate) }
            var monthSelection by remember { mutableStateOf(currentDate.month) }
            var yearSelection by remember { mutableIntStateOf(currentDate.year) }

            val state = rememberWeekCalendarState(
                startDate = startDate,
                endDate = endDate,
                firstVisibleWeekDate = currentDate,
                firstDayOfWeek = firstDayOfWeek
            )

            var coroutine = rememberCoroutineScope()
            monthSelection = state.firstVisibleWeek.days.first().date.month
            yearSelection = state.firstVisibleWeek.days.first().date.year

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${monthSelection.getDisplayName(TextStyle.FULL, Locale.FRANCE)
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }} $yearSelection",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = EUGreen120,
                    modifier = Modifier
                        .clickable {
                            coroutine.launch {
                                selection = currentDate
                                state.animateScrollToWeek(currentDate)
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            /*Text(
                currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )*/

            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors =
                CardDefaults.cardColors(
                    containerColor = EUGreen100
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                WeekCalendar(
                    /*modifier = Modifier
                        .drawBehind {
                            drawRoundRect(
                                EUGreen20,
                                cornerRadius = CornerRadius(10.dp.toPx())
                            )
                        }
                    ,*/
                    state = state,
                    dayContent = { day ->
                        Day(day.date, isSelected = selection == day.date, medicationNumber = treatmentOfTheDay(treatments, notifications, day.date).size) { clicked ->
                            if (selection != clicked) {
                                selection = clicked
                            }
                        }
                    }
                ) {
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Column(
                modifier = Modifier
                    .verticalScroll(
                        enabled = true,
                        state = rememberScrollState()
                    )
            ) {
                var dailyTreatment = treatmentOfTheDay(treatments, notifications, selection)
                val sortedDailyTreatment = sortedTreatment(dailyTreatment)
                if (dailyTreatment.isNotEmpty()) {
                    for (treatment in sortedDailyTreatment) {
                        val heureFormatee = String.format("%02d:%02d", treatment.second, treatment.third)
                        MedicationCalendarCard(
                            heureFormatee,
                            treatment.first,
                            painScale = true,
                            active = true
                        )

                        Spacer(modifier = Modifier.height(15.dp))
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Aucun traitement pour cette journée",
                            fontStyle = FontStyle.Italic,
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                /*MedicationCalendarCard(
                    "10h00",
                    "Médicament exemple",
                    painScale = true,
                    active = true
                )

                Spacer(modifier = Modifier.height(15.dp))

                MedicationCalendarCard(
                    selection.toString(),
                    "Médicament exemple",
                    painScale = true,
                    active = false
                )

                Spacer(modifier = Modifier.height(15.dp))

                MedicationCalendarCard(
                    "12h00",
                    "Médicament exemple",
                    painScale = false,
                    active = true
                )

                Spacer(modifier = Modifier.height(15.dp))

                MedicationCalendarCard(
                    "13h00",
                    "Médicament exemple",
                    painScale = false,
                    active = false
                )

                Spacer(modifier = Modifier.height(15.dp))

                MedicationCalendarCard(
                    "14h00",
                    "Médicament exemple",
                    painScale = true,
                    active = true
                )

                Spacer(modifier = Modifier.height(15.dp))

                MedicationCalendarCard(
                    "15h00",
                    "Médicament exemple",
                    painScale = true,
                    active = false
                )*/
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarPreview() {
    Calendar(mutableListOf(), mutableListOf())
}


fun treatmentOfTheDay(treatments: List<Treatment>, notifications: List<Notification>, day : LocalDate) : MutableMap<String, Pair<MutableList<Int>,MutableList<Int>>> {

    var result = mutableMapOf<String, Pair<MutableList<Int>,MutableList<Int>>>()
    for (notification in notifications) {
        var durationNotif = notification.medicationName!!.duration!!
        if (durationNotif.startDate <= day && durationNotif.endDate >= day) {
            var treatment = notification.medicationName!!
            var posology = treatment.posology.split(" ")
            //var quantity = posology[0]
            //var dateFrequency : LocalDate? = null
            if (posology[3] == "jour") {
                result[treatment.medication!!.name] = Pair(notification.hours, notification.minutes)
            } else if (posology[3] == "semaine") {
                if (notification.frequency.contains(day.dayOfWeek)) {
                    result[treatment.medication!!.name] = Pair(notification.hours, notification.minutes)
                }
            } else {
                if (((day.dayOfYear - notification.medicationName!!.duration!!.startDate.dayOfYear) % 30) == 0) {
                    result[treatment.medication!!.name] = Pair(notification.hours, notification.minutes)
                }
            }
        }
    }

    return result
}


fun sortedTreatment(treatments: MutableMap<String, Pair<MutableList<Int>,MutableList<Int>>>) : List<Triple<String,Int,Int>> {
    var sortedList = mutableListOf<Triple<String,Int,Int>>()

    for (key in treatments.keys) {
        for (index in 0 until treatments[key]!!.first.size) {
            sortedList.add(Triple(key, treatments[key]!!.first[index], treatments[key]!!.second[index]))
        }
    }

    sortedList.sortWith(compareBy({ it.second }, { it.third }))

    return sortedList
}