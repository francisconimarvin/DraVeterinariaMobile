package com.example.draveterinaria.data.model

data class Mascota(
    val id: Int? = null,
    val nombre: String = "",
    val especie: String = "",
    val raza: String = "",
    val edad: String = "",
    val idTutor: Int? = null
)