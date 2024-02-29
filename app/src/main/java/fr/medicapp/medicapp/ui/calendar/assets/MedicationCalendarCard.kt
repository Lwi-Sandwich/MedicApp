package fr.medicapp.medicapp.ui.calendar.assets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.medicapp.medicapp.ui.theme.EUGreen100
import fr.medicapp.medicapp.ui.theme.EUGreen120
import fr.medicapp.medicapp.ui.theme.EUGreen140
import fr.medicapp.medicapp.ui.theme.EUGreen20
import fr.medicapp.medicapp.ui.theme.EUGreen40
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MedicationCalendarCard(
    hour : String,
    medication : String,
    painScale : Boolean,
    active : Boolean
) {
    var activeCard by remember { mutableStateOf(active) }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .combinedClickable(
                onClick = { },
                onLongClick = {
                    activeCard = !activeCard
                }
            ),
        colors =
        CardDefaults.cardColors(
            containerColor = EUGreen100,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Medication,
                    contentDescription = "",
                    tint = if (activeCard) Color.White else EUGreen40,
                )
                Text(
                    medication,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    fontStyle = if (activeCard) FontStyle.Normal else FontStyle.Italic,
                    color = if (activeCard) Color.White else EUGreen40,
                    textDecoration = if (activeCard) TextDecoration.None else TextDecoration.LineThrough
                )
            }
            Text(
                hour,
                fontSize = 17.sp,
                color = if (activeCard) Color.White else EUGreen40,
                fontStyle = if (activeCard) FontStyle.Normal else FontStyle.Italic
            )

            if (painScale) {
                var sliderValueBefore by remember { mutableFloatStateOf(0f) }
                var sliderValueAfter by remember { mutableFloatStateOf(0f) }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Avant",
                        color = if (activeCard) Color.White else EUGreen40,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontStyle = if (activeCard) FontStyle.Normal else FontStyle.Italic
                    )
                    Text(
                        " - Niveau de douleur ressenti : ",
                        color = if (activeCard) Color.White else EUGreen40,
                        fontSize = 16.sp,
                        fontStyle = if (activeCard) FontStyle.Normal else FontStyle.Italic
                    )
                    Text(
                        (sliderValueBefore + 0.1).toInt().toString(),
                        fontWeight = FontWeight.Bold,
                        color = if (activeCard) Color.White else EUGreen40,
                        fontSize = 16.sp,
                        fontStyle = if (activeCard) FontStyle.Normal else FontStyle.Italic
                    )
                }

                if (activeCard) {
                    Slider(
                        value = sliderValueBefore,
                        onValueChange = { sliderValueBefore = it },
                        valueRange = 0f..10f,
                        steps = 9,
                        colors = SliderDefaults.colors(
                            thumbColor = EUGreen140,
                            activeTrackColor = EUGreen140,
                            inactiveTrackColor = EUGreen20,
                        ),
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Après",
                        color = if (activeCard) Color.White else EUGreen40,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontStyle = if (activeCard) FontStyle.Normal else FontStyle.Italic
                    )
                    Text(
                        " - Niveau de douleur ressenti : ",
                        color = if (activeCard) Color.White else EUGreen40,
                        fontSize = 16.sp,
                        fontStyle = if (activeCard) FontStyle.Normal else FontStyle.Italic
                    )
                    Text(
                        (sliderValueAfter + 0.1).toInt().toString(),
                        fontWeight = FontWeight.Bold,
                        color = if (activeCard) Color.White else EUGreen40,
                        fontSize = 16.sp,
                        fontStyle = if (activeCard) FontStyle.Normal else FontStyle.Italic
                    )
                }

                if (activeCard) {
                    Slider(
                        value = sliderValueAfter,
                        onValueChange = { sliderValueAfter = it },
                        valueRange = 0f..10f,
                        steps = 9,
                        colors = SliderDefaults.colors(
                            thumbColor = EUGreen140,
                            activeTrackColor = EUGreen140,
                            inactiveTrackColor = EUGreen20,
                        ),
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun MedicationCalendarCardPreview() {
    MedicationCalendarCard(
        "10h00",
        "Médicament test",
        painScale = true,
        active = true)
}