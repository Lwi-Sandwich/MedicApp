package fr.medicapp.medicapp.ui.prescription.EditPrescription

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import fr.medicapp.medicapp.entity.MedicationEntity
import fr.medicapp.medicapp.model.Duration
import fr.medicapp.medicapp.model.Treatment
import fr.medicapp.medicapp.ui.prescription.SearchDialog
import fr.medicapp.medicapp.ui.theme.EUBlack100
import fr.medicapp.medicapp.ui.theme.EUBlue100
import fr.medicapp.medicapp.ui.theme.EUGreen100
import fr.medicapp.medicapp.ui.theme.EUOrange100
import fr.medicapp.medicapp.ui.theme.EUPurple100
import fr.medicapp.medicapp.ui.theme.EUPurple20
import fr.medicapp.medicapp.ui.theme.EUPurple60
import fr.medicapp.medicapp.ui.theme.EUPurple80
import fr.medicapp.medicapp.ui.theme.EURed100
import fr.medicapp.medicapp.ui.theme.EURed60
import java.time.LocalDate

/**
 * Cette fonction affiche une carte de traitement avec des informations spécifiques.
 *
 * @param treatment Le traitement à afficher sur la carte.
 * @param onRemove La fonction à exécuter lorsque l'utilisateur supprime le traitement.
 * @param medications La liste des médicaments à afficher.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreatmentCard(
    treatment: Treatment,
    onRemove: () -> Unit,
    medications: List<MedicationEntity>
) {
    var darkmode : Boolean = isSystemInDarkTheme()
    var medication = remember { mutableStateOf(treatment.medication?.name ?: "") }
    var notification = remember { mutableStateOf(treatment.notification) }
    var duration = remember { mutableStateOf(treatment.duration?.toString() ?: "") }
    var posology = remember { mutableStateOf(treatment.posology) }
    var renew = remember { mutableStateOf(treatment.renew) }
    var quantity = remember { mutableStateOf(treatment.quantity) }

    LaunchedEffect(treatment) {
        medication.value = treatment.medication?.name ?: ""
        notification.value = treatment.notification
        duration.value = treatment.duration?.toString() ?: ""
        posology.value = treatment.posology
        renew.value = treatment.renew
        quantity.value = treatment.quantity
    }

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
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                var medicationOpen by remember { mutableStateOf(false) }

                if (medicationOpen) {
                    SearchDialog(
                        options = medications.map { it.toOptionDialog() },
                        cardColor = EUPurple20,
                        selectedCardColor = EUPurple80,
                        onDismiss = {
                            medicationOpen = false
                        },
                        onValidate = {
                            medicationOpen = false
                            medication.value = it.title
                            treatment.medication = medications.find { medication -> medication.cisCode == it.id }
                        },
                        preQuery = treatment.query
                    )
                }

                OutlinedTextField(
                    enabled = false,
                    value = medication.value,
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = EUBlack100
                    ),
                    onValueChange = { },
                    trailingIcon = {
                        if (treatment.query != "" && medication.value == "") {
                            Icon(
                                imageVector = Icons.Filled.WarningAmber,
                                contentDescription = "",
                                tint = EURed100
                            )
                        }
                    },
                    label = { Text("Nom du médicament") },
                    shape = RoundedCornerShape(20),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EUPurple100,
                        unfocusedBorderColor = EUPurple100,
                        disabledBorderColor = EUPurple100,
                        errorBorderColor = EURed60,
                        focusedLabelColor = EUPurple100,
                        unfocusedLabelColor = EUPurple100,
                        disabledLabelColor = EUPurple100,
                        errorLabelColor = EURed60,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            medicationOpen = true
                        }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = {
                        onRemove()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EUPurple60,
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(30),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .size(57.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            /*if (i.erreur.isNotEmpty()) {
                Row {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "",
                        tint = EURed100,
                        modifier = Modifier
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        i.erreur,
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light,
                        color = EURed100
                    )
                }
            }*/

            Row {
                Switch(
                    checked = notification.value,
                    onCheckedChange = {
                        notification.value = it
                        treatment.notification = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = EUGreen100,
                        uncheckedBorderColor = EURed100,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = EURed100,
                    ),
                )
                Spacer(modifier = Modifier.width(7.dp))
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                ) {
                    Text(
                        "Notification de rappel ${if (treatment.notification) "activée" else "désactivée"}",
                        fontSize = 18.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .size(24.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .background(color = EUGreen100)
                                .clip(RoundedCornerShape(100.dp)),
                            imageVector = Icons.Filled.HourglassTop,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))

                    var durationOpen by remember { mutableStateOf(false) }

                    if (durationOpen) {
                        CalendarDialog(
                            state = rememberUseCaseState(true, onCloseRequest = {
                                durationOpen = false
                            }),
                            selection = CalendarSelection.Period { startDate, endDate ->
                                treatment.duration = Duration(startDate, endDate)
                                duration.value = treatment.duration.toString()
                                durationOpen = false
                            },
                        )
                    }

                    Column() {
                        Row() {
                            // Options pour les dropdowns
                            val numbers = listOf(1,2,3,4,5,6,7,8,9,10)
                            val units = listOf("fois", "comprimé", "gélule") // Ajouter d'autres options au besoin
                            val periodicities = listOf("jour", "semaine", "mois")

                            var isDropDownMenuNumbersOpen by remember { mutableStateOf(false) }
                            var isDropDownMenuUnitsOpen by remember { mutableStateOf(false) }
                            var isDropDownMenuPeriodicitiesOpen by remember { mutableStateOf(false) }
                            var selectedNumber by remember { mutableStateOf(numbers[0]) }
                            var selectedUnits by remember { mutableStateOf(units[0]) }
                            var selectedPeriodicities by remember { mutableStateOf(periodicities[0]) }

                            /*OutlinedTextField(
                                value = posology.value,
                                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                                onValueChange = {
                                    posology.value = it
                                    treatment.posology = it
                                },
                                label = { Text("Posologie") },
                                shape = RoundedCornerShape(20),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EUPurple100,
                                    unfocusedBorderColor = EUPurple100,
                                    disabledBorderColor = EUPurple100,
                                    errorBorderColor = EURed60,
                                    focusedLabelColor = EUPurple100,
                                    unfocusedLabelColor = EUPurple100,
                                    disabledLabelColor = EUPurple100,
                                    errorLabelColor = EURed60,
                                ),
                                modifier = Modifier.weight(1f)
                            )*/

                            ExposedDropdownMenuBox(
                                expanded = isDropDownMenuNumbersOpen,
                                onExpandedChange = { isDropDownMenuNumbersOpen = !isDropDownMenuNumbersOpen},
                                modifier = Modifier.weight(1f)) {
                                TextField(
                                    modifier = Modifier.menuAnchor(),
                                    value = selectedNumber.toString(),
                                    onValueChange = {},
                                    readOnly = true,
                                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                                    maxLines = 1,
                                    trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = isDropDownMenuNumbersOpen
                                    )}
                                    )

                                ExposedDropdownMenu(expanded = isDropDownMenuNumbersOpen, onDismissRequest = { isDropDownMenuNumbersOpen = false }) {
                                    numbers.forEach{ number ->
                                        DropdownMenuItem(
                                            text = { Text(text = number.toString()) },
                                            onClick = {
                                                selectedNumber = number
                                                isDropDownMenuNumbersOpen = false
                                            },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                        )
                                    }
                                }
                            }
                            ExposedDropdownMenuBox(expanded = isDropDownMenuUnitsOpen,
                                onExpandedChange = { isDropDownMenuUnitsOpen = !isDropDownMenuUnitsOpen},
                                modifier = Modifier.weight(1f)) {
                                TextField(
                                    modifier = Modifier.menuAnchor(),
                                    value = selectedUnits,
                                    onValueChange = {},
                                    readOnly = true,
                                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                                    maxLines = 1,
                                    trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = isDropDownMenuUnitsOpen,
                                    )}
                                )

                                ExposedDropdownMenu(expanded = isDropDownMenuUnitsOpen, onDismissRequest = { isDropDownMenuUnitsOpen = false }) {
                                    units.forEach{ unit ->
                                        DropdownMenuItem(
                                            text = { Text(text = unit) },
                                            onClick = {
                                                selectedUnits = unit
                                                isDropDownMenuUnitsOpen = false
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "par",
                                style = TextStyle(fontSize = 16.sp, color = Color.Black),
                                modifier = Modifier
                                    .weight(0.5f)
                                    .align(Alignment.CenterVertically),
                                textAlign = TextAlign.Center
                            )

                            ExposedDropdownMenuBox(expanded = isDropDownMenuPeriodicitiesOpen,
                                onExpandedChange = { isDropDownMenuPeriodicitiesOpen = !isDropDownMenuPeriodicitiesOpen},
                                modifier = Modifier.weight(1f)) {
                                TextField(
                                    modifier = Modifier.menuAnchor(),
                                    value = selectedPeriodicities.toString(),
                                    onValueChange = {},
                                    readOnly = true,
                                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                                    maxLines = 1,
                                    /*trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = isDropDownMenuPeriodicitiesOpen
                                    )}*/
                                    trailingIcon = {
                                        IconButton(
                                            onClick = { isDropDownMenuPeriodicitiesOpen = !isDropDownMenuPeriodicitiesOpen },
                                            modifier = Modifier.align(Alignment.CenterVertically),
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = "Menu déroulant",
                                                tint = Color.Black, // Couleur de l'icône
                                                modifier = Modifier.size(24.dp) // Taille personnalisée de l'icône
                                            )
                                        }
                                    }
                                )

                                ExposedDropdownMenu(expanded = isDropDownMenuPeriodicitiesOpen, onDismissRequest = { isDropDownMenuPeriodicitiesOpen = false }) {
                                    periodicities.forEach { periodiciti ->
                                        DropdownMenuItem(
                                            text = { Text(text = periodiciti.toString()) },
                                            onClick = {
                                                selectedPeriodicities = periodiciti
                                                isDropDownMenuPeriodicitiesOpen = false
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                        )
                                    }
                                }
                            }
                        }

                        OutlinedTextField(
                            enabled = false,
                            value = duration.value,
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            onValueChange = { },
                            label = { Text("Durée") },
                            shape = RoundedCornerShape(20),
                            trailingIcon = {
                                if (treatment.query != "" && duration.value == Duration(LocalDate.now(), LocalDate.now()).toString()) {
                                    Icon(
                                        imageVector = Icons.Filled.WarningAmber,
                                        contentDescription = "",
                                        tint = EURed100
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EUPurple100,
                                unfocusedBorderColor = EUPurple100,
                                disabledBorderColor = EUPurple100,
                                errorBorderColor = EURed60,
                                focusedLabelColor = EUPurple100,
                                unfocusedLabelColor = EUPurple100,
                                disabledLabelColor = EUPurple100,
                                errorLabelColor = EURed60,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    durationOpen = true
                                }
                        )

                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(end = 15.dp)
                                .size(24.dp)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .background(color = EUBlue100)
                                    .clip(RoundedCornerShape(100.dp)),
                                imageVector = Icons.Filled.Repeat,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp))

                        OutlinedTextField(
                            value = renew.value,
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            onValueChange = {
                                renew.value = it
                                treatment.renew = it
                            },
                            label = { Text("Renouvellement") },
                            shape = RoundedCornerShape(20),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EUPurple100,
                                unfocusedBorderColor = EUPurple100,
                                disabledBorderColor = EUPurple100,
                                errorBorderColor = EURed60,
                                focusedLabelColor = EUPurple100,
                                unfocusedLabelColor = EUPurple100,
                                disabledLabelColor = EUPurple100,
                                errorLabelColor = EURed60,
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .size(24.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .background(color = EUOrange100)
                                .clip(RoundedCornerShape(100.dp)),
                            imageVector = Icons.Filled.Medication,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    OutlinedTextField(
                        value = quantity.value,
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        onValueChange = {
                            quantity.value = it
                            treatment.quantity = it
                        },
                        label = { Text("Quantité suffisante pour...") },
                        shape = RoundedCornerShape(20),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EUPurple100,
                            unfocusedBorderColor = EUPurple100,
                            disabledBorderColor = EUPurple100,
                            errorBorderColor = EURed60,
                            focusedLabelColor = EUPurple100,
                            unfocusedLabelColor = EUPurple100,
                            disabledLabelColor = EUPurple100,
                            errorLabelColor = EURed60,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            //fin padding
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun TreatmentCardPreview() {
    TreatmentCard(
        treatment = Treatment(
            duration = null,
            notification = false,
        ),
        onRemove = {},
        medications = listOf()
    )
}