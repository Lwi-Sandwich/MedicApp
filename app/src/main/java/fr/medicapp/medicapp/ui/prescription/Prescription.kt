package fr.medicapp.medicapp.ui.prescription

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Sick
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.medicapp.medicapp.entity.MedicationEntity
import fr.medicapp.medicapp.entity.TreatmentEntity
import fr.medicapp.medicapp.model.Duration
import fr.medicapp.medicapp.model.InfosMedication
import fr.medicapp.medicapp.model.Treatment
import fr.medicapp.medicapp.ui.theme.EUBlue100
import fr.medicapp.medicapp.ui.theme.EUGreen100
import fr.medicapp.medicapp.ui.theme.EUGreen40
import fr.medicapp.medicapp.ui.theme.EUGrey100
import fr.medicapp.medicapp.ui.theme.EUOrange100
import fr.medicapp.medicapp.ui.theme.EUOrange110
import fr.medicapp.medicapp.ui.theme.EUPurple20
import fr.medicapp.medicapp.ui.theme.EUPurple80
import fr.medicapp.medicapp.ui.theme.EURed100
import fr.medicapp.medicapp.ui.theme.EURed40
import fr.medicapp.medicapp.ui.theme.EUYellow100
import java.time.LocalDate

/**
 * Cette fonction affiche la prescription avec des informations spécifiques.
 *
 * @param consultation La liste des traitements pour la consultation.
 * @param onClose La fonction à exécuter lorsque l'utilisateur ferme la prescription.
 * @param onRemove La fonction à exécuter lorsque l'utilisateur supprime la prescription.
 * @param onUpdate La fonction à exécuter lorsque l'utilisateur met à jour la prescription.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Prescription(
    consultation : MutableList<Treatment>,
    informations: MutableMap<String, InfosMedication> = mutableMapOf(),
    onClose: () -> Unit,
    onRemove: () -> Unit,
    onUpdate: (String, Boolean) -> Unit
) {
    var darkmode : Boolean = isSystemInDarkTheme()
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Unspecified
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp)
                        .weight(1f)
                ) {
                    Button(
                        onClick = {
                            onClose()
                        },
                        shape = RoundedCornerShape(20),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EUOrange110,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f)
                    ) {
                        Text(
                            text = "Retour",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.3f))

                    Button(
                        onClick = onRemove,
                        shape = RoundedCornerShape(20),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EURed100,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f)
                    ) {
                        Row() {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Supprimer",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(
                    enabled = true,
                    state = rememberScrollState()
                )
        ) {
            // Itération de la liste des médicaments
            for (i in consultation) {
                val infos = informations[i.medication?.cisCode ?: ""]
                var notification = remember {
                    mutableStateOf(i.notification)
                }
                Text(
                    i.medication?.name ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(5.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = EUPurple20,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Switch(
                                enabled = false,
                                checked = notification.value,
                                onCheckedChange = {
                                    notification.value = it
                                    onUpdate(i.id, it)
                                },
                                colors = SwitchDefaults.colors(
                                    disabledCheckedThumbColor = Color.White,
                                    disabledCheckedTrackColor = EUGreen40,
                                    disabledUncheckedBorderColor = EURed40,
                                    disabledUncheckedThumbColor = Color.White,
                                    disabledUncheckedTrackColor = EURed40,
                                ),
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 10.dp)
                            ) {
                                Text(
                                    "Notification de rappel ${if (i.notification) "activée" else "désactivée"}",
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = EUPurple20,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Row() {
                            Box(
                                modifier = Modifier
                                    .background(color = EUGreen100)
                                    .clip(RoundedCornerShape(100.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.HourglassTop,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                "Posologie : ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                i.posology,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))

                        if (i.renew != "") {
                            Row() {
                                Icon(
                                    imageVector = Icons.Filled.Repeat,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .background(color = EUBlue100)
                                        .clip(RoundedCornerShape(20.dp)),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    "A renouveler : ",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    i.renew.toString() + " fois",
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                        }

                        if (i.quantity != "") {
                            Row() {
                                Icon(
                                    imageVector = Icons.Filled.Medication,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .background(color = EUOrange100)
                                        .clip(RoundedCornerShape(20.dp)),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    "Quantité suffisante pour : ",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    i.quantity,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = EUPurple80)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Informations",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                // Affichage des informations thérapeutiques
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = EUPurple20,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Row() {
                            Box(
                                modifier = Modifier
                                    .background(color = EUGreen100)
                                    .clip(RoundedCornerShape(100.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AutoFixHigh,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                "Informations thérapeutiques ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))

                        /*if (i.therapeutic != "") {
                            Text(
                                i.therapeutic,
                                fontSize = 18.sp
                            )
                        } else {*/
                            Text(
                                infos?.indications_therapeutiques?:
                                "Aucune information thérapeutique pour ce médicament.",
                                fontSize = 18.sp
                            )
                        //}
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                //Affichage des principes actifs
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = EUPurple20,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Row() {
                            Box(
                                modifier = Modifier
                                    .background(color = EUBlue100)
                                    .clip(RoundedCornerShape(100.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Biotech,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                "Principes actifs ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))

                        /*if (i.activePrinciple != "") {
                            Text(
                                i.activePrinciple,
                                fontSize = 18.sp
                            )
                        } else {*/
                            Text(
                                infos?.principes_actifs?.joinToString(", ")?:
                                "Aucun principe actif connu pour ce médicament.",
                                fontSize = 18.sp
                            )
                        //}
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                // Affichage des risques
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = EUPurple20,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Row() {
                            Box(
                                modifier = Modifier
                                    .background(color = EURed100)
                                    .clip(RoundedCornerShape(100.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                "Classification ATC",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))

                        /*if (i.contraindication != "") {
                            Text(
                                i.contraindication,
                                fontSize = 18.sp
                            )
                        } else {*/
                            Text(
                                infos?.classifcation_atc?:
                                "Classification ATC inconnue pour ce médicament.",
                                fontSize = 18.sp
                            )
                        //}
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                // Affichage des informations complémentaires
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = EUPurple20,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Row() {
                            Box(
                                modifier = Modifier
                                    .background(color = EUGrey100)
                                    .clip(RoundedCornerShape(100.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Help,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                "Renseignements complémentaires",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))

                        /*if (i.additionalInformation != "") {
                            Text(
                                i.additionalInformation,
                                fontSize = 18.sp
                            )
                        } else {*/
                            Text(
                                infos?.details?:
                                "Aucune information complémentaire pour ce médicament.",
                                fontSize = 18.sp
                            )
                        //}
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrescriptionPreview() {
    var tab = mutableListOf<Treatment>(
        Treatment(
        "1",
        MedicationEntity(
            "1",
            "PARACETAMOL TEVA 500 MG, GELULE",
            "500mg",
            listOf("Paracétamol", "Doliprane"),
            "Douleurs",
            "Paracétamol",
            "Douleurs",
            LocalDate.now(),
            "Douleurs",
            "Paracétamol",
            listOf("Douleurs", "Fièvre"),
            false
        ),
        "1 comprimé toutes les 6 heures",
        "3",
        "10",
        Duration(LocalDate.now(), LocalDate.now().plusDays(5)),
        false
    )
    )
    Prescription(tab, mutableMapOf(), {}, {}, { _, _ -> })
}