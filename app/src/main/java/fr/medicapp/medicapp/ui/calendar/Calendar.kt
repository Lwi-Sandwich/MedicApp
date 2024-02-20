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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
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

            WeekCalendar(
                modifier = Modifier
                    .drawBehind {
                        drawRoundRect(
                            EUGreen20,
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                ,
                state = state,
                dayContent = { day -> Day(day.date, isSelected = selection == day.date) { clicked ->
                        if (selection != clicked) {
                            selection = clicked
                        }
                    }
                }
            ) {
            }

            Spacer(modifier = Modifier.height(15.dp))

            MedicationCalendarCard(
                "10h00",
                "Médicament exemple"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarPreview() {
    Calendar()
}
