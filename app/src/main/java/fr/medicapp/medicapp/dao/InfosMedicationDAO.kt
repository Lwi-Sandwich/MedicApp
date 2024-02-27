package fr.medicapp.medicapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import fr.medicapp.medicapp.entity.InfosMedicationEntity

@Dao
interface InfosMedicationDAO {

    @Query("SELECT * FROM InfosMedication")
    fun getAll(): List<InfosMedicationEntity>

    @Query("SELECT * FROM InfosMedication WHERE cisCode = :id")
    fun getOne(id: String): InfosMedicationEntity

    @Insert
    fun add(t: InfosMedicationEntity)

    @Delete
    fun delete(t: InfosMedicationEntity)

    @Update
    fun update(t: InfosMedicationEntity)
}