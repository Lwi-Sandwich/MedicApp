package fr.medicapp.medicapp.model

import fr.medicapp.medicapp.entity.InfosMedicationEntity

data class InfosMedication(
    val cisCode: String,
    val indications_therapeutiques: String = "",
    val classifcation_atc: String = "",
    val principes_actifs: List<String> = listOf(),
    val details: String = ""
) {
    fun toEntity() = InfosMedicationEntity(
        cisCode,
        indications_therapeutiques,
        classifcation_atc,
        principes_actifs,
        details
    )

    companion object {
        fun fromEntity(entity: InfosMedicationEntity) = InfosMedication(
            entity.cisCode,
            entity.indications_therapeutiques,
            entity.classifcation_atc,
            entity.principes_actifs,
            entity.details
        )
    }
}