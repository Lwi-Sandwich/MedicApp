package fr.medicapp.medicapp.ui.calendar.assets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.medicapp.medicapp.ui.calendar.Calendar
import fr.medicapp.medicapp.ui.notifications.NotificationsEdit.getFrenchDayOfWeek
import fr.medicapp.medicapp.ui.theme.EUGreen120
import fr.medicapp.medicapp.ui.theme.EUGreen80
import fr.medicapp.medicapp.ui.theme.EURed120
import java.time.LocalDate


/**
 * Fonction d'affichage du jour pour le calendrier
 * @param day Un jour de la semaine
 * @author Quentin
 */

/*
    Exemple :

    LU  MA  ME  JE  VE  SA  DI
    10  11  12  13  14  15  16
*/

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Day(day: LocalDate, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Jour de la semaine en français
            Text(
                text = getFrenchDayOfWeek(day.dayOfWeek).take(2)
            ) // Transformation de la chaîne : MONDAY => LUNDI => LU

            // Numéro du jour
            Text(
                text = day.dayOfMonth.toString(),
                color = if (isSelected) EUGreen120 else Color.Unspecified,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = false)
@Composable
private fun DayPreview() {
    Day(LocalDate.now() ,isSelected = true)
}