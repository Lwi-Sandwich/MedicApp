package fr.medicapp.medicapp.ui.doctors

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import fr.medicapp.medicapp.model.Doctor
import fr.medicapp.medicapp.model.Notification
import fr.medicapp.medicapp.ui.doctors.assets.DoctorCard
import fr.medicapp.medicapp.ui.notifications.NotificationsEdit.getFrenchDayOfWeek
import fr.medicapp.medicapp.ui.theme.EUPurple100
import fr.medicapp.medicapp.ui.theme.EUPurple60
import fr.medicapp.medicapp.ui.theme.EUPurple80
import fr.medicapp.medicapp.ui.theme.EUYellow100
import fr.medicapp.medicapp.ui.theme.EUYellow110
import fr.medicapp.medicapp.ui.theme.EUYellow120

/**
 * Cette fonction affiche le menu principal des docteurs.
 *
 * @param doctors La liste des docteurs à afficher.
 * @param onDoctorClick La fonction à exécuter lorsque l'utilisateur sélectionne un docteur.
 * @param addDoctor La fonction à exécuter lorsque l'utilisateur ajoute un docteur.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorsMainMenu(
    doctors : MutableList<Doctor>,
    onDoctorClick : (Doctor) -> Unit = {},
    addDoctor : () -> Unit = {}
) {
    var darkmode : Boolean = isSystemInDarkTheme()
    val context = LocalContext.current
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Unspecified,
                    titleContentColor = if (darkmode) Color.White else Color.Black,
                ),
                title = {
                    Text(
                        "Liste des docteurs",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        bottomBar = {

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = addDoctor,
                containerColor = EUPurple100
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    tint = Color.White
                )
            }

        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(10.dp)
        ) {
            if (doctors.isNotEmpty()){
                for (i in doctors) {
                    DoctorCard(
                        onDoctorClick = {
                            onDoctorClick(i)
                        },
                        doctor = i
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                ) {
                    Text(
                        "Vous n'avez pas créé de docteurs.\nPour en créer un, cliquez sur\nle bouton en bas.",
                        color = if (darkmode) EUPurple60 else EUPurple100,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}

/**
 * Cette fonction affiche un aperçu du menu principal des notifications.
 */
@Preview(showBackground = true)
@Composable
private fun DoctorsMainMenuPreview() {
    DoctorsMainMenu(mutableListOf(
        Doctor("1345", "MOTTU", "Jean-Marie"),
        Doctor("2390", "NACHOUKI", "Gilles"),
        Doctor("1063", "BERDJUGIN", "Jean-François")
    )
    ) {}
}