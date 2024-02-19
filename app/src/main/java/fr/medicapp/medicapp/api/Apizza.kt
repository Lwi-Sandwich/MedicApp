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

    fun getDoctorByRpps(rpps: String): Doctor {
        val response = khttp.get("$apiUrl/$rpps")
        if (response.statusCode != 200) {
            throw Exception("Doctor not found")
        }
        val doctor = response.jsonObject
        val doc = Doctor(
            doctor.getString("idRpps"),
            doctor.getString("lastName"),
            doctor.getString("firstName"),
            doctor.getString("phoneNumber"),
            doctor.getString("email"),
            doctor.getString("specialty"),
            doctor.getString("zipcode").toInt(),
            doctor.getString("city"),
            doctor.getString("address")
        )
        if (!doc.isValid()) {
            throw Exception("Invalid doctor")
        }
        return doc
    }

}