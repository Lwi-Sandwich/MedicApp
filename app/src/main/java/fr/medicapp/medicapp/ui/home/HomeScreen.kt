package fr.medicapp.medicapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.medicapp.medicapp.model.Notification
import fr.medicapp.medicapp.ui.calendar.sortedTreatment
import fr.medicapp.medicapp.ui.calendar.treatmentOfTheDay
import fr.medicapp.medicapp.ui.home.assets.DayTreatment
import fr.medicapp.medicapp.ui.theme.EUGreen100
import fr.medicapp.medicapp.ui.theme.EUGreen120
import java.time.LocalDate

/**
 * Écran d'accueil de l'application MedicApp.
 *
 * Cet écran affiche un message de bienvenue à l'utilisateur et propose plusieurs actions :
 * - Ajouter une ordonnance
 * - Signaler un effet indésirable
 * - Ajouter un rappel
 *
 * @param onAddPrescriptionClick Fonction à appeler lorsque l'utilisateur clique sur le bouton "Ajouter une ordonnance".
 * @param onAddSideEffectClick Fonction à appeler lorsque l'utilisateur clique sur le bouton "Signaler un effet indésirable".
 * @param onAddNotification Fonction à appeler lorsque l'utilisateur clique sur le bouton "Ajouter un rappel".
 */
@Composable
fun HomeScreen(
    notifications : MutableList<Notification>,
    onAddPrescriptionClick: () -> Unit,
    onAddSideEffectClick: () -> Unit,
    onAddNotification: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bonjour, ",
                fontSize = 20.sp
            )
            Text(
                text = "Quentin Tegny",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = EUGreen120
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            colors = CardDefaults.cardColors(
                containerColor = EUGreen120,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Traitements du jour",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .verticalScroll(
                            enabled = true,
                            state = rememberScrollState()
                        )
                ) {
                    //TODO
                    // Mettre des cartes de traitement

                    var dailyTreatment = treatmentOfTheDay(mutableListOf(), notifications, LocalDate.now())
                    val sortedDailyTreatment = sortedTreatment(dailyTreatment)
                    for (treatment in sortedDailyTreatment) {
                        val heureFormatee = String.format("%02d:%02d", treatment.second, treatment.third)
                        DayTreatment(
                            enabled = true,
                            hour = heureFormatee,
                            medication = treatment.first
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    /*DayTreatment(
                        enabled = false,
                        hour = "15h",
                        medication = "Médicament test 2"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DayTreatment(
                        enabled = true,
                        hour = "16h",
                        medication = "Médicament test 3"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DayTreatment(
                        enabled = true,
                        hour = "18h",
                        medication = "Médicament test 4"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DayTreatment(
                        enabled = true,
                        hour = "20h",
                        medication = "Médicament test 5"
                    )*/
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onAddPrescriptionClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = EUGreen100
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = "Ajouter une ordonnance")
        }

        Button(
            onClick = onAddSideEffectClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = EUGreen100
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = "Signaler un effet indésirable")
        }

        Button(
            onClick = {
                onAddNotification()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = EUGreen100
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Ajouter un rappel",
            )
        }
    }
}

/**
 * Prévisualisation de l'écran d'accueil.
 *
 * Cette prévisualisation permet de voir à quoi ressemble l'écran d'accueil sans avoir à lancer l'application.
 */
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        notifications = mutableListOf(),
        onAddPrescriptionClick = { },
        onAddSideEffectClick = { },
        onAddNotification = {  }
    )
}
