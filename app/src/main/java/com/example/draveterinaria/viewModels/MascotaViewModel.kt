package com.example.draveterinaria.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.draveterinaria.data.model.Mascota
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MascotaViewModel : ViewModel() {

    private val _mascota = MutableStateFlow(Mascota())
    val mascota = _mascota.asStateFlow()

    private val _registrado = MutableStateFlow(false)
    val registrado = _registrado.asStateFlow()

    fun onNombreChange(value: String) {
        _mascota.value = _mascota.value.copy(nombre = value)
    }

    fun onEspecieChange(value: String) {
        _mascota.value = _mascota.value.copy(especie = value)
    }

    fun onRazaChange(value: String) {
        _mascota.value = _mascota.value.copy(raza = value)
    }

    fun onEdadChange(value: String) {
        _mascota.value = _mascota.value.copy(edad = value)
    }

    fun registrarMascota() {
        viewModelScope.launch {
            println("Mascota registrada: ${_mascota.value}")
            _registrado.value = true
        }
    }
}
