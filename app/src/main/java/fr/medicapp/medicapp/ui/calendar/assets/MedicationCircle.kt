package fr.medicapp.medicapp.ui.calendar.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.medicapp.medicapp.ui.theme.EURed100

@Composable
fun MedicationCircle() {
    Box(
        modifier = Modifier
            .padding(top = 37.dp, end = 30.dp)
            .size(7.dp)
            .background(color = EURed100, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
    }
}

@Preview(showBackground = false)
@Composable
fun MedicationCirclePreview() {
    MedicationCircle()
}