package fr.medicapp.medicapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entité représentant un médecin dans la base de données.
 *
 * @property id L'identifiant unique du médecin.
 * @property lastName Le nom de famille du médecin.
 * @property firstName Le prénom du médecin.
 */
@Entity
data class DoctorEntity(

    /**
     * L'identifiant unique du médecin.
     */
    @PrimaryKey
    val id: Int,

    /**
     * Le nom de famille du médecin.
     */
    val lastName: String,

    /**
     * Le prénom du médecin.
     */
    val firstName: String,

    /**
     * Le numéro de téléphone du médecin.
     */
    val phoneNumber: String?,

    /**
     * L'adresse email du médecin.
     */
    val email: String?,

    /**
     * La spécialité du médecin.
     */
    val specialty: String?,

    /**
     * Le code postal du médecin.
     */
    val zipCode: Int?,

    /**
     * La ville du médecin.
     */
    val city: String?,

    /**
     * L'adresse du médecin.
     */
    val address: String?
)
