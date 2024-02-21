package fr.medicapp.medicapp.api

import androidx.annotation.WorkerThread
import fr.medicapp.medicapp.model.Doctor
import org.json.JSONObject
import kotlin.reflect.typeOf

@WorkerThread
class Apizza private constructor() {

    companion object {
        private var instance: Apizza? = null

        fun getInstance(): Apizza {
            if (instance == null) {
                instance = Apizza()
            }
            return instance!!
        }
    }

    private val apiUrl = "https://data.instamed.fr/api/rpps"

    fun pingApi(): Boolean {
        val response = khttp.get("$apiUrl?_per_page=1")
        return response.statusCode == 200
    }

    private fun jsonToDoctor(json: JSONObject): Doctor {
        return Doctor(
            json.getString("idRpps"),
            json.getString("lastName"),
            json.getString("firstName"),
            json.getString("phoneNumber"),
            json.getString("email"),
            json.getString("specialty"),
            json.getString("zipcode").toInt(),
            json.getString("city"),
            json.getString("address")
        )
    }

    fun getDoctorByRpps(rpps: String): Doctor {
        val response = khttp.get("$apiUrl/$rpps")
        if (response.statusCode != 200) {
            throw Exception("Doctor not found")
        }
        val doctor = response.jsonObject
        val doc = jsonToDoctor(doctor)
        if (!doc.isValid()) {
            throw Exception("Invalid doctor")
        }
        return doc
    }

    fun getDoctorsByName(name: String): List<Doctor> {
        val response = khttp.get(
            apiUrl,
            params = mapOf("search" to name.lowercase(), "_per_page" to "100")
        )
        if (response.statusCode != 200) {
            return listOf()
        }
        try {
            val doctors = response.jsonObject.getJSONArray("hydra:member")
            val list = mutableListOf<Doctor>()
            for (i in 0 until doctors.length()) {
                try {
                    val doc = jsonToDoctor(doctors.getJSONObject(i))
                    if (doc.isValid()) {
                        list.add(doc)
                    }
                } catch (e: Exception) {
                    continue
                }
            }
            return list.toList()
        } catch (e: Exception) {
            return listOf()
        }
    }
}