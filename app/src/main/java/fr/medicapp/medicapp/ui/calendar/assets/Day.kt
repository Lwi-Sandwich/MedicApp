package fr.medicapp.medicapp.ui.calendar.assets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.medicapp.medicapp.ui.calendar.Calendar
import fr.medicapp.medicapp.ui.notifications.NotificationsEdit.getFrenchDayOfWeek
import fr.medicapp.medicapp.ui.theme.EUGreen120
import fr.medicapp.medicapp.ui.theme.EUGreen140
import fr.medicapp.medicapp.ui.theme.EUGreen20
import fr.medicapp.medicapp.ui.theme.EUGreen80
import fr.medicapp.medicapp.ui.theme.EURed100
import fr.medicapp.medicapp.ui.theme.EURed120
import java.time.LocalDate


/**
 * Fonction d'affichage du jour pour le calendrier
 * @param day Un jour de la semaine
 * @author Quentin
 */

/*
    Exemple :
    LUN  MAR  MER  JEU  VEN  SAM  DIM
     1    2    3    4    5    6    7
*/

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Day(
    day: LocalDate,
    isSelected: Boolean,
    medicationNumber : Int,
    onClick : (LocalDate) -> Unit

) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(5.dp)
            .wrapContentHeight()
            .clickable { onClick(day) }
            .drawBehind {
                drawRoundRect(
                    if (isSelected) EUGreen120 else EUGreen20,
                    cornerRadius = CornerRadius(10.dp.toPx())
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (medicationNumber > 0) MedicationCircle()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Jour de la semaine en français
            Text(
                text = getFrenchDayOfWeek(day.dayOfWeek).take(3),
                color = if (isSelected) Color.White else Color.Unspecified,
                fontSize = 13.sp
            ) // Transformation de la chaîne : MONDAY => LUNDI => LUN

            // Numéro du jour
            Text(
                text = day.dayOfMonth.toString(),
                //color = if (isSelected) EUGreen120 else Color.Unspecified,
                color = if (isSelected) Color.White else Color.Unspecified,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )
        }
        /*if (isSelected) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(5.dp)
                    .background(EUGreen80)
                    .align(Alignment.BottomCenter),
            )
        }*/
    }
}

@Preview(showBackground = false)
@Composable
private fun DayPreview() {
    Day(LocalDate.now() ,isSelected = true, 1) {LocalDate.now()}
}