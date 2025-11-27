package com.example.draveterinaria.data.model

// Clase que representa solo un ID (útil para Tutor y Especie en un POST/Request)
data class TutorIdWrapper(
    val idTutor: Long
)
data class EspecieIdWrapper(
    val idEspecie: Long
)
data class MascotaIdWrapper(
    val idMascota: Long
)
data class SubtipoIdWrapper(
    val idSubtipo: Long
)