package com.example.draveterinaria.data.model

// Representa el estado del formulario del Tutor
data class TutorInput(
    val rut: String,
    val nombre: String,
    val snombre: String,
    val apaterno: String,
    val amaterno: String,
    val telefono: String,
    val direccion: String,
    val email: String,
    // No necesitas confirmarEmail aquí
)

// Representa el estado del formulario de la Mascota
data class MascotaInput(
    val especieId: Long, // Solo el ID seleccionado
    val nombre: String,
    val fechaNacimiento: String,
    val raza: String,
    val sexo: String,
    val antecedentes: String
)

// Representa el estado del formulario del Servicio
data class ServicioInput(
    val tipoId: Long,
    val subtipoId: Long, // Solo el ID seleccionado
    val precio: Int, // Puede ser Int o Double, usa el tipo que manejes para el precio
    val fecha: String
)