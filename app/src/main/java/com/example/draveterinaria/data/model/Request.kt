package com.example.draveterinaria.data.model

data class TutorRequest(
    val runTutor: String,
    val dvRun: String, // Asumes que el backend separa el RUT
    val nombreTutor: String,
    val snombreTutor: String,
    val apPaternoTutor: String,
    val aMaternoTutor: String,
    val telefono: String,
    val direccion: String,
    val email: String
)

data class TutorResponse(
    val idTutor: Long

)

// Para POST /api/mascotas
data class MascotaRequest(
    val nombreMascota: String,
    val fechaNacimiento: String, // String "YYYY-MM-DD"
    val sexo: String,
    val raza: String,
    val antecedentes: String,

    // Referencias a ID (usan los wrappers definidos arriba)
    val especie: EspecieIdWrapper,
    val tutor: TutorIdWrapper
)

data class MascotaResponse(
    val idMascota: Long // CLAVE: Se usa para el POST final del servicio
)

// Para POST /api/servicios/servicios
data class ServicioRequest(
    val mascota: MascotaIdWrapper,
    val subtipo: SubtipoIdWrapper,
    val costo: Double,
    val fecha: String
)