package fr.medicapp.medicapp.model

import com.maxkeppeler.sheets.option.models.Option

/**
 * Modèle représentant un docteur.
 *
 * @property id L'identifiant unique du docteur.
 * @property lastName Le nom de famille du docteur.
 * @property firstName Le prénom du docteur.
 */
data class Doctor(

    /**
     * L'identifiant unique du docteur.
     */
    val id: String,

    /**
     * Le nom de famille du docteur.
     */
    val lastName: String,

    /**
     * Le prénom du docteur.
     */
    val firstName: String,

    /**
     * Le numéro de téléphone du docteur.
     */
    val phoneNumber: String = "",

    /**
     * L'adresse email du docteur.
     */
    val email: String = "",

    /**
     * La spécialité du docteur.
     */
    val specialty: String = "",

    /**
     * Le code postal du docteur.
     */
    val zipCode: Int = 0,

    /**
     * La ville du docteur.
     */
    val city: String = "",

    /**
     * L'adresse du docteur.
     */
    val address: String = ""
) {

    /**
     * Renvoie le nom complet du docteur.
     *
     * @return Le nom complet du docteur.
     */
    fun getFullName(): String {
        return "$firstName $lastName"
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères du docteur.
     *
     * @return Une chaîne de caractères représentant le docteur.
     */
    override fun toString(): String {
        return getFullName()
    }

    /**
     * Vérifie si le docteur est valide.
     *
     * @return `true` si le docteur est valide, `false` sinon.
     */
    fun isValide(): Boolean {
        return lastName.isNotEmpty() && firstName.isNotEmpty()
    }

    /**
     * Renvoie une option représentant le docteur.
     *
     * @return Une option représentant le docteur.
     */
    fun getOption(): Option {
        return Option(
            titleText = getFullName(),
        )
    }
}
