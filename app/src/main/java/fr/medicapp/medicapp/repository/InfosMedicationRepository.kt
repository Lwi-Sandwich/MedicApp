package fr.medicapp.medicapp.repository

import fr.medicapp.medicapp.dao.InfosMedicationDAO
import fr.medicapp.medicapp.entity.InfosMedicationEntity

class InfosMedicationRepository(
    private val infosMedicationDao: InfosMedicationDAO
) {
    fun getAll(): List<InfosMedicationEntity> {
        return infosMedicationDao.getAll()
    }

    fun getOne(id: String): InfosMedicationEntity {
        return infosMedicationDao.getOne(id)
    }

    fun add(t: InfosMedicationEntity) {
        infosMedicationDao.add(t)
    }

    fun delete(t: InfosMedicationEntity) {
        infosMedicationDao.delete(t)
    }

    fun update(t: InfosMedicationEntity) {
        infosMedicationDao.update(t)
    }
}