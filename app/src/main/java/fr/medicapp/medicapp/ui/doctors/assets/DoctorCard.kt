package fr.medicapp.medicapp.ui.doctors.assets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.medicapp.medicapp.model.Doctor
import fr.medicapp.medicapp.ui.theme.EUPurple80

/**
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorCard(
    onDoctorClick : (String) -> Unit = {},
    doctor : Doctor
) {
    ElevatedCard(
        onClick = {
            onDoctorClick(doctor.id!!)
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors =
        CardDefaults.cardColors(
            containerColor = EUPurple80,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
        ) {

            Row() {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "",
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = doctor.getFullName(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold

                )
            }

            Text(
                text = doctor.specialty,
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}