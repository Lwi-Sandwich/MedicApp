package fr.medicapp.medicapp.ui.doctors

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import fr.medicapp.medicapp.model.Doctor
import fr.medicapp.medicapp.ui.doctors.assets.DoctorCard
import fr.medicapp.medicapp.ui.theme.EUGreen100
import fr.medicapp.medicapp.ui.theme.EUGreen40
import fr.medicapp.medicapp.ui.theme.EUOrange110
import fr.medicapp.medicapp.ui.theme.EUPurple100
import fr.medicapp.medicapp.ui.theme.EUPurple20
import fr.medicapp.medicapp.ui.theme.EUPurple40
import fr.medicapp.medicapp.ui.theme.EUPurple60
import fr.medicapp.medicapp.ui.theme.EUPurple80
import fr.medicapp.medicapp.ui.theme.EURed60
import fr.medicapp.medicapp.ui.theme.EUWhite100

/**
 * Cette fonction affiche l'ajout d'un docteur.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorsAdd(
    doctorsList: List<Pair<Doctor, Int>> = listOf(),
    onSearch: (String, Boolean) -> Unit = {_,_->},
    onCheckDistance: (Boolean) -> Unit = {},
    getDistance: (Doctor) -> Int = { 0 },
    onCancel: () -> Unit = {},
    onConfirm: (Doctor) -> Unit = {}
) {

    val doctorSelected = remember { mutableStateOf<Doctor?>(null) }
    val darkmode : Boolean = isSystemInDarkTheme()
    val name = remember { mutableStateOf("") }
    val checkedState = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Unspecified,
                    titleContentColor = if (darkmode) Color.White else Color.Black,
                ),
                title = {
                    Text(
                        "Ajouter un docteur",
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
                        onClick = onCancel,
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
                            text = "Annuler",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.3f))

                    Button(
                        enabled = doctorSelected.value != null,
                        onClick = {
                            doctorSelected.value?.let { onConfirm(it) }
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
                            text = "Ajouter",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .wrapContentSize()
                .padding(10.dp)
        ) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = EUPurple80,
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
                    Text(
                        "Recherche d'un médecin par nom",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )

                    OutlinedTextField(
                        value = name.value,
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                        onValueChange = {
                            name.value = it
                            doctorSelected.value = null
                        },
                        label = { Text("Nom du médecin") },
                        shape = RoundedCornerShape(20),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EUWhite100,
                            unfocusedBorderColor = EUWhite100,
                            disabledBorderColor = EUWhite100,
                            errorBorderColor = EURed60,
                            focusedLabelColor = EUWhite100,
                            unfocusedLabelColor = EUWhite100,
                            disabledLabelColor = EUWhite100,
                            errorLabelColor = EURed60,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = {
                                checkedState.value = it
                                onCheckDistance(it)
                            },
                            colors = CheckboxDefaults.colors(
                                uncheckedColor = EUPurple20,
                                checkedColor = EUPurple40,
                                checkmarkColor = Color.White
                            )
                        )

                        Text(
                            "Trier par distance",
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.End
                        )

                        Button(
                            onClick = {
                                onSearch(name.value, checkedState.value)
                                doctorSelected.value = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EUGreen100,
                                contentColor = Color.White
                            ),
                        ) {
                            Text(
                                "Rechercher",
                                color = Color.White,
                                fontSize = 16.sp,
                                textAlign = TextAlign.End
                            )

                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Début de la carte d'essai avec le docteur
            Column(
                modifier = Modifier.verticalScroll(enabled=true, state=rememberScrollState())
            ) {
                doctorsList.forEach { (doc, distance) ->
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (doctorSelected.value?.id == doc.id) EUPurple80 else EUPurple60,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            if (doctorSelected.value?.id == doc.id) {
                                doctorSelected.value = null
                            } else {
                                doctorSelected.value = doc
                            }
                        }
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        if (distance < 0) "?" else "$distance",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "km",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }


                                Spacer(modifier = Modifier.width(15.dp))

                                Text(
                                    "${doc.firstName.replaceFirstChar { it.uppercase() }} ${doc.lastName.uppercase()}",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    // Fin de la carte
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorsAddPreview() {
    DoctorsAdd()
}