package fr.medicapp.medicapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InfosMedication")
data class InfosMedicationEntity(

    @PrimaryKey
    val cisCode: String,

    val indications_therapeutiques: String = "",

    val classifcation_atc: String = "",

    val principes_actifs: List<String> = listOf(),

    val details: String = ""
) {
}