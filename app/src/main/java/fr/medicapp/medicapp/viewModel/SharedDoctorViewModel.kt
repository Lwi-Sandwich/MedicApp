package fr.medicapp.medicapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getDoctorsByName(api: Apizza, name: String) {
            if (name.isEmpty()) {
                _sharedStateList.value = emptyList()
                return
            }
            val docs = api.getDoctorsByName(name)
            _sharedStateList.value = docs.map { Pair(it, -1) }.sortedBy { it.first.lastName }
    }
}