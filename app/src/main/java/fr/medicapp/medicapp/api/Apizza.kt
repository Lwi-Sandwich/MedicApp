package fr.medicapp.medicapp.api

import androidx.annotation.WorkerThread

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
}