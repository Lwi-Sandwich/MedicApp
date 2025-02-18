package fr.medicapp.medicapp.ui.sideeffectsdiary

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.medicapp.medicapp.model.OptionDialog
import fr.medicapp.medicapp.model.SideEffect
import fr.medicapp.medicapp.model.Treatment
import fr.medicapp.medicapp.ui.prescription.DatePickerModal
import fr.medicapp.medicapp.ui.prescription.EditPrescription.AddButton
import fr.medicapp.medicapp.ui.prescription.SearchDialog
import fr.medicapp.medicapp.ui.prescription.TimePickerModal
import fr.medicapp.medicapp.ui.sideeffectsdiary.SEDEdit.SearchDialogWithOption
import fr.medicapp.medicapp.ui.theme.EUGreen100
import fr.medicapp.medicapp.ui.theme.EUGreen40
import fr.medicapp.medicapp.ui.theme.EUOrange110
import fr.medicapp.medicapp.ui.theme.EURed100
import fr.medicapp.medicapp.ui.theme.EURed20
import fr.medicapp.medicapp.ui.theme.EURed40
import fr.medicapp.medicapp.ui.theme.EURed60
import fr.medicapp.medicapp.ui.theme.EURed80
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Cette fonction est utilisée pour éditer un effet secondaire.
 * Elle utilise le thème sombre du système pour définir les couleurs de l'interface utilisateur.
 *
 * @param sideeffects L'effet secondaire à éditer.
 * @param treatments Une liste mutable de traitements à afficher.
 * @param onConfirm Une fonction lambda qui est appelée lorsque l'utilisateur clique sur le bouton "Confirmer".
 * @param onCancel Une fonction lambda qui est appelée lorsque l'utilisateur clique sur le bouton "Annuler".
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SEDEdit(
    sideeffects: SideEffect,
    treatments: MutableList<Treatment>,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    var darkmode : Boolean = isSystemInDarkTheme()
    var nomMedicament by remember { mutableStateOf(sideeffects.medicament?.medication?.name ?: "") }

    var errorDialogOpen = remember { mutableStateOf(false) }

    if (errorDialogOpen.value) {
        AlertDialog(
            onDismissRequest = {
                errorDialogOpen.value = false
            },
            title = {
                Text("Erreur")
            },
            text = {
                Text("Veuillez remplir tous les champs")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        errorDialogOpen.value = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Unspecified,
                    titleContentColor = if (darkmode) Color.White else Color.Black,
                ),
                title = {
                    Text(
                        "Ajouter un nouvel effet",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
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
                            onCancel()
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
                        onClick = {
                            if (nomMedicament != null && sideeffects.date != null && sideeffects.hour != null && sideeffects.minute != null && sideeffects.effetsConstates.size > 0 && sideeffects.effetsConstates.all { it != "" }) {
                                onConfirm()
                            } else {
                                errorDialogOpen.value = true
                            }
                        },
                        shape = RoundedCornerShape(20),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EUGreen100,
                            contentColor = Color.White,
                            disabledContainerColor = EUGreen40,
                            disabledContentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f)
                    ) {
                        Text(
                            text = "Valider",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

        },
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .verticalScroll(
                    enabled = true,
                    state = rememberScrollState()
                )
        ) {
            ElevatedCard(
                onClick = { },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors =
                CardDefaults.cardColors(
                    containerColor = EURed80,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp),
                ) {
                    var treatmentOpen by remember { mutableStateOf(false) }

                    if (treatmentOpen) {
                        SearchDialog(
                            options = treatments.map { it.toOptionDialog() }.toMutableList(),
                            cardColor = EURed20,
                            selectedCardColor = EURed80,
                            onDismiss = {
                                treatmentOpen = false
                            },
                            onValidate = { option ->
                                sideeffects.medicament = treatments.find { it.id == option.id }
                                nomMedicament = option.title
                                treatmentOpen = false
                            }
                        )
                    }
                    
                    OutlinedTextField(
                        enabled = false,
                        value = nomMedicament,
                        textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color= Color.White),
                        onValueChange = {},
                        label = { Text("Nom du médicament") },
                        shape = RoundedCornerShape(20),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            disabledBorderColor = Color.White,
                            disabledLabelColor = Color.White,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable {
                                treatmentOpen = true
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            ElevatedCard(
                onClick = { },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 115.dp),
                colors =
                CardDefaults.cardColors(
                    containerColor = EURed80,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Signalement :",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    var datePrescriptionOpen by remember { mutableStateOf(false) }
                    var datePrescriptionState = rememberDatePickerState()

                    if (datePrescriptionOpen) {
                        DatePickerModal(
                            state = datePrescriptionState,
                            onDismissRequest = {
                                datePrescriptionOpen = false
                            },
                            onConfirm = {
                                datePrescriptionOpen = false
                                if (datePrescriptionState.selectedDateMillis != null) {
                                    sideeffects.date = Instant.ofEpochMilli(datePrescriptionState.selectedDateMillis!!).atZone(
                                        ZoneId.systemDefault()
                                    ).toLocalDate()
                                }
                            }
                        )
                    }

                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

                    Row() {
                        OutlinedTextField(
                            enabled = false,
                            value = if (sideeffects.date != null) sideeffects.date!!.format(formatter) else "",
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = Color.White
                            ),
                            onValueChange = { },
                            label = { Text("Date") },
                            shape = RoundedCornerShape(20),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.CalendarMonth,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                disabledBorderColor = Color.White,
                                disabledLabelColor = Color.White,
                            ),
                            modifier = Modifier
                                .width(200.dp)
                                .clickable {
                                    datePrescriptionOpen = true
                                }
                        )

                        Spacer(modifier = Modifier.width(10.dp))


                        var frequencyTimeOpen = remember { mutableStateOf(false) }
                        var frequencyTimeState = rememberTimePickerState(
                            is24Hour = true,
                        )

                        if (frequencyTimeOpen.value) {
                            TimePickerModal(
                                state = frequencyTimeState,
                                clockBackgroundColor = EURed20,
                                selectorColor = EURed100,
                                timeSelectorSelectedContainerColor = EURed40,
                                timeSelectorUnselectedContainerColor = EURed20,
                                onDismissRequest = {
                                    frequencyTimeOpen.value = false
                                },
                                onConfirm = {
                                    sideeffects.hour = frequencyTimeState.hour
                                    Log.d("Hour", sideeffects.hour.toString())
                                    sideeffects.minute = frequencyTimeState.minute
                                    Log.d("Minute", sideeffects.minute.toString())
                                    frequencyTimeOpen.value = false
                                }
                            )
                        }

                        OutlinedTextField(
                            modifier = Modifier.clickable{
                                frequencyTimeOpen.value = true
                            },
                            enabled = false,
                            value = if (sideeffects.hour != null && sideeffects.minute != null) "${sideeffects.hour}h${if (sideeffects.minute!! < 9) "0"+sideeffects.minute else sideeffects.minute}" else "",
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = Color.White
                            ),
                            onValueChange = { },
                            label = { Text("Heure") },
                            shape = RoundedCornerShape(20),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Alarm,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                disabledBorderColor = Color.White,
                                disabledLabelColor = Color.White,
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            ElevatedCard(
                onClick = { /*TODO*/ },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors =
                CardDefaults.cardColors(
                    containerColor = EURed80,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                ) {
                    Text(
                        text = "Effets constatés (${sideeffects.effetsConstates.size}) :",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    for (i in 0 until sideeffects.effetsConstates.size) {
                        var effetsConstates = remember { mutableStateOf(sideeffects.effetsConstates[i]) }
                        var sideEffectsOpen by remember { mutableStateOf(false) }
                        var listeEffets : MutableList<String> = mutableListOf(
                            "Mal de tête",
                            "Mal de ventre",
                            "Ballonnement",
                            "Vomissements",
                            "Nausées",
                            "Diarrhée",
                            "Constipation",
                            "Fatigue",
                            "Somnolence",
                            "Insomnie",
                            "Réaction allergique",
                            "Brûlures d'estomac",
                            "Sécheresse de la bouche",
                            "Sécheresse du nez",
                            "Sensibilité au soleil"
                        )
                        // TODO
                        //A déplacer dans une liste sauvegardée en base

                        if (sideEffectsOpen) {
                            SearchDialogWithOption(
                                title = "Qu'avez vous ressenti ?",
                                options = listeEffets.map {
                                    OptionDialog(id = it, title = it)
                                }.toMutableList(),
                                cardColor = EURed40,
                                selectedCardColor = EURed100,
                                onDismiss = {
                                    sideEffectsOpen = false
                                },
                                onValidate = { option ->
                                    effetsConstates.value = option.title
                                    sideeffects.effetsConstates[i] = option.title
                                    sideEffectsOpen = false
                                }
                            )
                        }

                        LaunchedEffect(sideeffects.effetsConstates[i]) {
                            effetsConstates.value = sideeffects.effetsConstates[i]
                        }

                        OutlinedTextField(
                            enabled = false,
                            value = effetsConstates.value,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = Color.White
                            ),
                            onValueChange = {
                                /*effetsConstates.value = it
                                sideeffects.effetsConstates[i] = effetsConstates.value*/
                            },
                            shape = RoundedCornerShape(20),
                            trailingIcon = {
                                IconButton(onClick = { sideeffects.effetsConstates.removeAt(i) }) {
                                    Icon(
                                        imageVector = Icons.Filled.DeleteForever,
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                disabledBorderColor = Color.White,
                                disabledLabelColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    sideEffectsOpen = true
                                }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    AddButton(
                        text = "Ajouter un effet",
                        icone = Icons.Filled.Add,
                        color = EURed60,
                        onClick = {
                            sideeffects.effetsConstates.add("")
                        }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun SEDEditPreview() {
    var se = SideEffect()
    var treatments = mutableListOf<Treatment>()
    //var se = listOf<TestSideEffect>()
    SEDEdit(se, treatments, {}) {}
}