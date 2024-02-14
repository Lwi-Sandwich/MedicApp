package fr.medicapp.medicapp.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import fr.medicapp.medicapp.ui.calendar.assets.Day
import fr.medicapp.medicapp.ui.theme.EUGreen20
import java.time.LocalDate
import java.time.YearMonth

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
            val startDate = remember { currentMonth.minusMonths(100).atStartOfMonth() } // Adjust as needed
            val endDate = remember { currentMonth.plusMonths(100).atEndOfMonth() } // Adjust as needed
            val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library
            var selection by remember { mutableStateOf(currentDate) }

            val state = rememberWeekCalendarState(
                startDate = startDate,
                endDate = endDate,
                firstVisibleWeekDate = currentDate,
                firstDayOfWeek = firstDayOfWeek
            )

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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarPreview() {
    Calendar()
}
