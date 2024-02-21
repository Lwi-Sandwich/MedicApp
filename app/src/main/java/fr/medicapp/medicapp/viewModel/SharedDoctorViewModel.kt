package fr.medicapp.medicapp.viewModel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import fr.medicapp.medicapp.api.Apizza
import fr.medicapp.medicapp.model.Doctor
import fr.medicapp.medicapp.model.Notification
import fr.medicapp.medicapp.repository.DoctorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class SharedDoctorViewModel: ViewModel() {
    private val _sharedState = MutableStateFlow(Doctor("1", "", ""))
    val sharedState = _sharedState.asStateFlow()

    private val _sharedStateList = MutableStateFlow(listOf<Pair<Doctor, Int>>())
    val sharedStateList = _sharedStateList.asStateFlow()



    fun getDoctor(id: String, repo: DoctorRepository) {
        try {
            val doctor = repo.getOne(id).toDoctor()
            _sharedState.value = doctor
        } catch (e: Exception) {
            _sharedState.value = Doctor("1", "Erreur", "Erreur")
        }
    }

    fun getDoctorsByName(api: Apizza, name: String, sort: Boolean, context: Context) {
            if (name.isEmpty()) {
                _sharedStateList.value = emptyList()
                return
            }
            var docs = api.getDoctorsByName(name).map { Pair(it, -1)}.sortedBy { it.first.lastName }
            val geocoder = Geocoder(context, Locale.getDefault())
            if (sort) {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Handle location result
                        if (location != null) {
                            docs = docs.map { (doc, dist) ->
                                var d2 = dist
                                try {
                                    val loc = geocoder.getFromLocationName(
                                        "${doc.address} ${doc.zipCode} ${doc.city}",
                                        1
                                    )
                                    if (loc?.isNotEmpty() == true) { // Le == true est vraiment nécessaire, je sais coder.
                                        val l = Location("")
                                        l.latitude = loc.first().latitude
                                        l.longitude = loc.first().longitude
                                        d2 = location.distanceTo(l).toInt() / 1000
                                    }
                                } catch (e: Exception) {
                                    println(e)
                                    d2 = -1
                                }
                                Pair(doc, d2)
                            }
                            _sharedStateList.value = docs.sortedBy { it.second }
                        }
                    }
            }
            _sharedStateList.value = docs.sortedBy { it.second }
    }
}