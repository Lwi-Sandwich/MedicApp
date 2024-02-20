package fr.medicapp.medicapp.repository

import fr.medicapp.medicapp.dao.DoctorDAO
import fr.medicapp.medicapp.dao.DurationDAO
import fr.medicapp.medicapp.entity.DoctorEntity
import fr.medicapp.medicapp.entity.DurationEntity

/**
 * Repository pour gérer les opérations de base de données pour les entités Doctor.
 *
 * @property doctorDao L'objet DAO pour accéder aux méthodes de base de données.
 */
class DurationRepository(

    /**
     * L'objet DAO pour accéder aux méthodes de base de données.
     */
    private val durationDAO: DurationDAO
) {

    /**
     * Récupère tous les docteurs de la base de données.
     *
     * @return Une liste de toutes les entités Doctor dans la base de données.
     */
    fun getAll(): List<DurationEntity> {
        return durationDAO.getAll()
    }

    /**
     * Récupère un docteur spécifique de la base de données.
     *
     * @param id L'identifiant unique du docteur à récupérer.
     * @return L'entité Doctor correspondant à l'identifiant donné.
     */
    fun getOne(id: Int): DurationEntity {
        return durationDAO.getOne(id)
    }

    /**
     * Ajoute une nouvelle entité Doctor à la base de données.
     *
     * @param t L'entité Doctor à ajouter à la base de données.
     */
    fun add(t: DurationEntity) {
        durationDAO.add(t)
    }

    /**
     * Supprime une entité Doctor spécifique de la base de données.
     *
     * @param t L'entité Doctor à supprimer de la base de données.
     */
    fun delete(t: DurationEntity) {
        durationDAO.delete(t)
    }

    /**
     * Met à jour une entité Doctor spécifique dans la base de données.
     *
     * @param t L'entité Doctor à mettre à jour dans la base de données.
     */
    fun update(t: DurationEntity) {
        durationDAO.update(t)
    }
}
