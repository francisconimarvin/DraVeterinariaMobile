package com.example.draveterinaria.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.draveterinaria.data.model.Tutor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TutorViewModel : ViewModel() {

    private val _tutor = MutableStateFlow(Tutor())
    val tutor = _tutor.asStateFlow()

    private val _registrado = MutableStateFlow(false)
    val registrado = _registrado.asStateFlow()

    fun onNombreChange(value: String) {
        _tutor.value = _tutor.value.copy(nombre = value)
    }

    fun onCorreoChange(value: String) {
        _tutor.value = _tutor.value.copy(correo = value)
    }

    fun onTelefonoChange(value: String) {
        _tutor.value = _tutor.value.copy(telefono = value)
    }

    fun registrarTutor() {
        viewModelScope.launch {
            // Simulación de registro
            println("Tutor registrado: ${_tutor.value}")
            _registrado.value = true
        }
    }
}