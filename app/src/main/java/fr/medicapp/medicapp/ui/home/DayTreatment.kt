package fr.medicapp.medicapp.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import fr.medicapp.medicapp.ui.theme.EUGreen100
import fr.medicapp.medicapp.ui.theme.EUGreen40

@Composable
fun DayTreatment(
    enabled : Boolean,
    hour : String,
    medication : String
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = EUGreen100,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Medication,
                    contentDescription = "",
                    tint = if (enabled) Color.White else EUGreen40
                )
                Spacer(modifier = Modifier.width(5.dp))
                if (enabled) {
                    Text(
                        "$hour - $medication",
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        "$hour - $medication",
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.LineThrough,
                        fontStyle = FontStyle.Italic,
                        color = EUGreen40
                    )
                }
            }
        }
    }
}